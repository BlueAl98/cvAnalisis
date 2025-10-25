package com.blue.cvAnalisis.service;

import com.blue.cvAnalisis.model.ApiResponse;
import com.blue.cvAnalisis.model.DocumentResponse;
import com.blue.cvAnalisis.model.EmailRequest;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class AnalisisService {

    private final RabbitTemplate rabbitTemplate;

    private final WebClient webClient;

    @Value("${spring.application.base-url-micro-send}")
    private String baseUrlMicroSend;

    @Value("${spring.application.base-url-micro-extract}")
    private String baseUrlMicroExtract;

    @Value("${spring.application.base-url-micro-ai}")
    private String baseUrlAi;


    public AnalisisService(RabbitTemplate rabbitTemplate, WebClient webClient) {
        this.webClient = webClient;
        this.rabbitTemplate = rabbitTemplate;
    }

    public EmailRequest sendEmailRequest(EmailRequest emailRequest) {
        rabbitTemplate.convertAndSend("email.exchange", "email.send", emailRequest);
        System.out.println("📤 Sent message to RabbitMQ: " + emailRequest);
        return emailRequest;
    }

    public String analisisCv(MultipartFile file) {

        System.out.println(extracText(file).getExtractedText());
        return "CV analyzed successfully";

    }


    @RabbitListener(queues = "email.queue")
    public void receiveEmailMessage(EmailRequest emailRequest) {
        try {
            Mono<ApiResponse<EmailRequest>> response = webClient.post()
                    .uri(baseUrlMicroExtract+"/email")
                    .bodyValue(emailRequest) // send as JSON
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<>() {
                    });

            // If you want to block and wait for response (optional)
            ApiResponse<EmailRequest> result = response.block();
            System.out.println("Response from microservice: " + result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public DocumentResponse extracText (MultipartFile file) {
        try {

            MultipartBodyBuilder builder = new MultipartBodyBuilder();
            builder.part("file", new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename(); // necessary for multipart
                }
            });

            Mono<ApiResponse<DocumentResponse>> response = webClient.post()
                    .uri(baseUrlMicroExtract+"/documents")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .bodyValue(builder.build())
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<>() {
                    });

            // If you want to block and wait for response (optional)
            ApiResponse<DocumentResponse> result = response.block();
            return result.getData();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


}
