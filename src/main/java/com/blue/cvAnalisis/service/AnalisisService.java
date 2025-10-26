package com.blue.cvAnalisis.service;

import com.blue.cvAnalisis.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Slf4j
@Service
public class AnalisisService {

    private static final String EMAIL_EXCHANGE = "email.exchange";
    private static final String EMAIL_ROUTING_KEY = "email.send";
    private static final String EMAIL_QUEUE = "email.queue";


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


    /**
     * 🔹 Main flow: extract text → analyze → send email
     */
    public ResponseEntity<ApiResponse<CvAnalysisResult>> analisisCv(MultipartFile file) {
        try {
            DocumentResponse extracted = extracText(file);
            if (extracted == null) {
                return new ResponseEntity<>(new ApiResponse<>(400, "Error analyzing CV text", null), HttpStatus.BAD_REQUEST);
            }

            CvAnalysisResult analysis = analizeCvText(extracted.getExtractedText(), "Android developer kotlin");
            if (analysis == null) {
                return new ResponseEntity<>(new ApiResponse<>(400, "Error analyzing CV text", null), HttpStatus.BAD_REQUEST);
            }

            EmailRequest email = new EmailRequest("alblue9817@gmail.com", "Test App", buildEmailBody(analysis));
            sendEmailRequest(email);
            return new ResponseEntity<>(new ApiResponse<>(200, "CV analyzed and email request sent", analysis), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse<>(400, "Error analyzing CV text", null), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 🔹 Send email request to RabbitMQ
     */
    public void sendEmailRequest(EmailRequest emailRequest) {
        rabbitTemplate.convertAndSend(EMAIL_EXCHANGE, EMAIL_ROUTING_KEY, emailRequest);
    }

    /**
     * 🔹 Listen for messages from the email queue
     */
    @RabbitListener(queues = EMAIL_QUEUE)
    public void receiveEmailMessage(EmailRequest emailRequest) {
        callPostApi(baseUrlMicroSend + "/email", emailRequest, new ParameterizedTypeReference<ApiResponse<EmailRequest>>() {});
    }

    /**
     * 🔹 Extract text from uploaded CV
     */
    public DocumentResponse extracText(MultipartFile file) {
        try {
            MultipartBodyBuilder builder = new MultipartBodyBuilder();
            builder.part("file", new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            });

            ApiResponse<DocumentResponse> response = callPostApi(
                    baseUrlMicroExtract + "/documents",
                    builder.build(),
                    new ParameterizedTypeReference<>() {
                    },
                    MediaType.MULTIPART_FORM_DATA
            );

            return response != null ? response.getData() : null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 🔹 Send text to AI microservice for analysis
     */
    public CvAnalysisResult analizeCvText(String cvText, String targetProfile) {
        ApiResponse<CvAnalysisResult> response = callPostApi(
                baseUrlAi + "/ai/cvAnalisis",
                new RequestAICv(cvText, targetProfile),
                new ParameterizedTypeReference<>() {
                }
        );
        return response != null ? response.getData() : null;
    }

    /**
     * 🔹 Generic POST call helper with optional content type
     */
    private <T> ApiResponse<T> callPostApi(String url, Object body, ParameterizedTypeReference<ApiResponse<T>> type) {
        return callPostApi(url, body, type, MediaType.APPLICATION_JSON);
    }

    private <T> ApiResponse<T> callPostApi(String url, Object body, ParameterizedTypeReference<ApiResponse<T>> type, MediaType mediaType) {
        try {
            Mono<ApiResponse<T>> response = webClient.post()
                    .uri(url)
                    .contentType(mediaType)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(type);

            ApiResponse<T> result = response.block();
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    private String buildEmailBody(CvAnalysisResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("📄 Candidate Analysis Report\n\n")
                .append("👤 Name: ").append(result.getCandidate().getName()).append("\n")
                .append("🧠 Summary: ").append(result.getCandidate().getSummary()).append("\n\n")
                .append("⚙️ Technical Match: ").append(result.getEvaluation().getTechnicalMatch()).append("%\n")
                .append("💼 Experience Match: ").append(result.getEvaluation().getExperienceMatch()).append("%\n")
                .append("⭐ Overall Score: ").append(result.getEvaluation().getOverallScore()).append("%\n")
                .append("🗣 Verdict: ").append(result.getEvaluation().getVerdict()).append("\n\n")
                .append("💪 Strengths:\n - ")
                .append(String.join("\n - ", result.getKeyPoints().getStrengths())).append("\n\n")
                .append("⚠️ Weaknesses:\n - ")
                .append(String.join("\n - ", result.getKeyPoints().getWeaknesses())).append("\n\n")
                .append("❓ Recommended Questions:\n - ")
                .append(String.join("\n - ", result.getKeyPoints().getRecommendedQuestions())).append("\n\n")
                .append("💡 Improvement Tips:\n - ")
                .append(String.join("\n - ", result.getImprovementTips()));

        return sb.toString();
    }
}
