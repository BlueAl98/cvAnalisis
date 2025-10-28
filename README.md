# 🧠 CV Analyzer - Microservices Application

This repository contains the **CV Analyzer**, a Spring Boot-based microservices application designed to **analyze candidate CVs**, **generate AI-powered reviews**, and **send personalized feedback via email**.

---

## 🚀 Overview

The system follows a **microservices architecture** and integrates multiple components:

- 🗂 **Document Extraction Service** – extracts text from uploaded CV files (PDF, DOCX, etc.).  
- 🤖 **AI Analysis Service** – analyzes the extracted text to evaluate the candidate’s strengths, weaknesses, and job fit.  
- 📧 **Email Service** – sends detailed AI-generated analysis reports via email.  
- 🐇 **RabbitMQ Message Broker** – handles asynchronous email processing.  

The main service orchestrates all these processes, ensuring scalability and fault tolerance.

---

## 🏗 Architecture Diagram

```
        [User Uploads CV]
                │
                ▼
     ┌──────────────────────┐
     │  CV Analyzer API     │
     │  (Spring Boot)       │
     └─────────┬────────────┘
               │
               ▼
   ┌──────────────────────────┐
   │  Document Extractor      │──► Extract text
   └──────────────────────────┘
               │
               ▼
   ┌──────────────────────────┐
   │  AI Analyzer Microservice│──► Analyze and score CV
   └──────────────────────────┘
               │
               ▼
   ┌──────────────────────────┐
   │  RabbitMQ (email queue)  │──► Async message
   └──────────────────────────┘
               │
               ▼
   ┌──────────────────────────┐
   │  Email Sender Service    │──► Send report
   └──────────────────────────┘
```

---

## ⚙️ Technologies Used

| Component | Technology |
|------------|-------------|
| Backend Framework | Spring Boot 3 (Java 17) |
| Messaging Queue | RabbitMQ |
| Reactive HTTP Client | Spring WebClient |
| AI/Analysis Logic | External AI microservice |
| File Handling | MultipartFile, Reactor |
| JSON Serialization | Jackson |
| Build Tool | Maven |
| Containerization | Docker + Docker Compose |

---

## 🧩 Main Features

- ✅ Upload and analyze CVs in PDF/DOCX format  
- ✅ AI-generated technical, experience, and overall match scores  
- ✅ Strengths, weaknesses, and improvement suggestions  
- ✅ Email delivery via RabbitMQ queue system  
- ✅ Concurrent request limiter using `Semaphore`  
- ✅ Modular microservice communication via REST APIs  
- ✅ Fully containerized microservices setup with Docker Compose  

---

## 🛠 Configuration

Environment variables or `.env` file must include:

```env
HOST_RABBITMQ=localhost
PORT_RABBITMQ=5672
USERNAME_RABBITMQ=guest
PASSWORD_RABBITMQ=guest

MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=youremail@gmail.com
MAIL_PASSWORD=yourpassword

SPRING_AI_MODEL=llama3
BASE_URL_AI=http://host.docker.internal:11434
```

---

## 🐳 Docker Compose Setup

Below is the `docker-compose.yml` file to manage all microservices and dependencies in containers.

```yaml
version: "3.9"
services:
  
  rabbitmq:
    image: rabbitmq:3-management
    container_name: rabbitmq
    ports:
      - "5672:5672"
      - "15672:15672"
    environment:
      RABBITMQ_DEFAULT_USER: ${USERNAME_RABBITMQ}
      RABBITMQ_DEFAULT_PASS: ${PASSWORD_RABBITMQ}

  micro-extract:
    image: userdockerhub/micro-extract
    container_name: micro-extract
    ports:
      - "8081:8081"

  micro-ai:
    image: userdockerhub/micro-ai:1.1
    container_name: micro-ai
    ports:
      - "8083:8083"    
    environment:
      SPRING_APP_NAME: micro-ai
      SPRING_AI_MODEL: ${SPRING_AI_MODEL}
      BASE_URL_AI: http://host.docker.internal:11434
      SERVER_PORT: 8083
    extra_hosts:
      - "host.docker.internal:host-gateway"    

  micro-send-message:
    image: userdockerhub/micro-send-message
    container_name: micro-send-message
    ports:
      - "8082:8082"
    environment: 
      MAIL_HOST: ${MAIL_HOST}
      MAIL_PORT: ${MAIL_PORT}
      MAIL_USERNAME: ${MAIL_USERNAME}
      MAIL_PASSWORD: ${MAIL_PASSWORD}

  cv-analisis:
    image: userdockerhub/cv-analisis:v1  
    container_name: cv-analisis
    ports:
      - "8080:8080"
    depends_on:
      - rabbitmq
      - micro-send-message
      - micro-ai
      - micro-extract
    environment:
      HOST_RABBITMQ: ${HOST_RABBITMQ}
      PORT_RABBITMQ: ${PORT_RABBITMQ}
      USERNAME_RABBITMQ: ${USERNAME_RABBITMQ}
      PASSWORD_RABBITMQ: ${PASSWORD_RABBITMQ}
      BASE_URL_MICRO_SEND: http://micro-send-message:8082
      BASE_URL_MICRO_AI: http://micro-ai:8083
      BASE_URL_MICRO_EXTRACT: http://micro-extract:8081    
```

### ▶ Run All Services

```bash
docker compose up -d
```

Once started, you can access:
- **CV Analyzer API:** http://localhost:8080  
- **AI Microservice:** http://localhost:8083  
- **Extractor Microservice:** http://localhost:8081  
- **Email Microservice:** http://localhost:8082  
- **RabbitMQ Dashboard:** http://localhost:15672 (user: guest / pass: guest)

---

## 🧪 API Endpoint

### **POST** `/api`

Uploads and analyzes a CV file.

#### Request (multipart/form-data):
| Field | Type | Description |
|-------|------|-------------|
| `file` | File | CV file (PDF/DOCX) |
| `data` | String | Email address to send report |
| `targetProfile` | String | Target job position |

#### Example using `curl`:
```bash
curl -X POST http://localhost:8080/api   -F "file=@/path/to/cv.pdf"   -F "data=johndoe@example.com"   -F "targetProfile=Backend Developer"
```

#### Example Response:
```json
{
  "status": 200,
  "message": "CV analyzed and email request sent",
  "data": {
    "candidate": { "name": "John Doe", "years_experience": 5, "summary": "Experienced Java Developer" },
    "evaluation": { "technical_match": 85, "experience_match": 90, "overall_score": 88, "verdict": "Strong candidate" },
    "key_points": {
      "strengths": ["Java", "Spring Boot"],
      "weaknesses": ["Cloud Experience"],
      "recommended_questions": ["Describe a RESTful API you built"]
    },
    "improvement_tips": ["Add more cloud experience"]
  }
}
```

---

## 📬 Example Email Output

```
📄 Candidate Analysis Report

👤 Name: John Doe
🧠 Summary: Experienced Java Developer

⚙️ Technical Match: 85%
💼 Experience Match: 90%
⭐ Overall Score: 88%
🗣 Verdict: Strong candidate

💪 Strengths:
 - Java
 - Spring Boot

⚠️ Weaknesses:
 - Cloud Experience

❓ Recommended Questions:
 - Describe a RESTful API you built

💡 Improvement Tips:
 - Add more cloud experience
```

---

## 🤝 Contributing

1. Fork this repository  
2. Create a new branch (`feature/your-feature`)  
3. Commit your changes  
4. Push to your branch  
5. Submit a Pull Request  

---

## 📜 License

This project is licensed under the **MIT License**. See the LICENSE file for details.

---

## 💬 Author

**Blue / Alex Loera**  
📧 [Your Email or GitHub Contact Link]  
🚀 Passionate about AI, backend systems, and automation.
