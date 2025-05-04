# Kahve Arası ☕📖

Her sabah kahveni yudumlarken, e-posta kutuna düşen edebi bir satır...

## 📌 Proje Amacı

**Kahve Arası**, kullanıcıların her sabah kısa bir kitap parçası, edebi bir satır veya ilham verici bir metni e-posta yoluyla almasını sağlayan bir içerik servisidir.  
Amaç: Güne edebiyatla, huzurla ve sade bir keyifle başlamalarını sağlamak. ☀️📬

---

## ⚙️ Kullanılan Teknolojiler

- Java 21
- Spring Boot 3
- PostgreSQL (Docker ile)
- Redis (içerik önbellekleme için _ eklenicek)
- RabbitMQ (mesaj kuyruğu ve asenkron işlemler için _ eklenicek)
- Spring Mail (e-posta gönderimi _ eklenicek)
- Docker Compose
- Swagger UI (API dokümantasyonu)
- GitHub Actions (CI/CD – ileride eklenecek)

---

## 🚀 Başlangıç

1. Gerekli servisleri başlat:

   ```bash
   docker-compose up -d
   ```

2. Spring Boot uygulamasını çalıştır:

   ```bash
   mvn spring-boot:run
   ```

3. Swagger arayüzüne erişim:

   ```
   http://localhost:8080/swagger-ui.html
   ```

4. Veritabanı bağlantısı ve yapılandırma, `application.yml` içinde yapılmaktadır. Geliştirme ortamı için örnek değerler kullanılmaktadır.

---

##  Not

⚠️ Bu yapılandırmalar sadece **geliştirme ortamı** içindir.  
Gerçek şifreler, bağlantı adresleri ve hassas bilgiler paylaşılmamaktadır.  
Gizli bilgiler ileride `.env` dosyasına taşınacak ve  
`application.yml` dosyası örnek sürümüyle (`application-sample.yml`) değiştirilecektir.

## 🔧 application.yml Yapılandırması

Bu projeyi çalıştırabilmek için `src/main/resources/application.yml` dosyasını **kendiniz oluşturmanız** gerekir.  
Aşağıdaki örnek şablona göre kendi bilgilerinizi girerek oluşturabilirsiniz:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5433/mydatabase
    username: myuser
    password: secret
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true
  rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: guest
    listener:
      direct:
        auto-startup: true
  mail:
    host: smtp.gmail.com
    port: 587
    username: your.email@gmail.com
    password: your_app_password
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
