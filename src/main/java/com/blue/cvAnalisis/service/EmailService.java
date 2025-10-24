package com.blue.cvAnalisis.service;

import com.blue.cvAnalisis.model.ApiResponse;
import com.blue.cvAnalisis.model.EmailRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class EmailService {

    private final RabbitTemplate rabbitTemplate;

    private final WebClient webClient;

    private final ObjectMapper objectMapper; // For converting JSON string to object


    public EmailService(RabbitTemplate rabbitTemplate, WebClient webClient, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.webClient = webClient;
        this.rabbitTemplate = rabbitTemplate;
    }

    public EmailRequest sendEmailRequest(EmailRequest emailRequest) {
        rabbitTemplate.convertAndSend("email.exchange", "email.send", emailRequest);
        System.out.println("📤 Sent message to RabbitMQ: " + emailRequest);
        return emailRequest;
    }


    @RabbitListener(queues = "email.queue")
    public void receiveEmailMessage(EmailRequest emailRequestString) {
        try {
            // Convert JSON string to EmailRequest object
          //  EmailRequest emailRequest = objectMapper.readValue(emailRequestString, EmailRequest.class);

            Mono<ApiResponse<EmailRequest>> response = webClient.post()
                    .uri("http://localhost:8082/email")
                    .bodyValue(emailRequestString) // send as JSON
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<ApiResponse<EmailRequest>>() {});

            // If you want to block and wait for response (optional)
            ApiResponse<EmailRequest> result = response.block();
            System.out.println("Response from microservice: " + result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
