# Kahve Arası ☕📖

Her sabah kahveni yudumlarken, e-posta kutuna düşen edebi bir satır...

## 📌 Proje Amacı

**Kahve Arası**, kullanıcıların her sabah kısa bir kitap parçası, edebi bir satır veya ilham verici bir metni e-posta yoluyla almasını sağlayan bir içerik servisidir.  
Amaç: Güne edebiyatla, huzurla ve sade bir keyifle başlamalarını sağlamak. ☀️📬

---

## ⚙️ Kullanılan Teknolojiler

- Java 21 , Spring Boot 3
- PostgreSQL 
- Redis 
- RabbitMQ 
- Spring Mail 
- ELK (ElasticSearch - Logstash- Kibana)
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
4. Kibana arayüzünden loglara ulaş:
   http://localhost:5601

5. Veritabanı bağlantısı ve yapılandırma, `application.yml` içinde yapılmaktadır. Geliştirme ortamı için örnek değerler kullanılmaktadır.

---

## Uygulama Senaryosu
1. Kullanıcılar sisteme sadece e-posta adresleriyle kayıt olur.
2. Günlük içerikler önceden sisteme yüklenmiştir.
3. Her sabah uygun zaman geldiğinde:
   * Gönderilecek içerik belirlenir.
   * RabbitMQ kuyruğuna mesaj bırakılır.
   * Consumer, bu mesajı alarak mail gönderir.
   * Gönderilen içerikler Redis’te kısa süreli, 
     veritabanında kalıcı olarak tutulur.

4. Aynı içerik bir kullanıcıya ikinci kez gönderilmez.

---
## Loglama Sistemi
* Logback, logları TCP soketi üzerinden Logstash'a gönderecek şekilde yapılandırılmıştır.
* Kibana, bu logları `spring-boot-logs` index'i üzerinden görselleştirir.
* INFO, WARN, ERROR seviyelerinde loglar Logstash aracılığıyla Elasticsearch’e iletilir.
* Kibana üzerinden filtreleme yapılabilir.

##  Not

⚠️ Bu yapılandırmalar sadece **geliştirme ortamı** içindir.  
Gerçek şifreler, bağlantı adresleri ve hassas bilgiler paylaşılmamaktadır.

---
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

  data:
    redis:
      host: localhost
      port: 6379
      connect-timeout: 1000
      lettuce:
        pool:
          max-active: 8
          max-idle: 8
          min-idle: 0

logging:
  level:
    root: INFO   # veya DEBUG seviyesine alabilirsin
    org.springframework.web: INFO
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} - %msg%n" # Konsol için sade bir format
  file:
    name: logs/application.log  # İstersen dosyaya da yazsın
```
---

## Medium Yazısı
Bu projeyle ilgili kurulum sürecini ve ELK Stack entegrasyonunu detaylı anlattığım yazı:

👉 [Logların Büyüsünü Keşfet: Spring Boot + Docker ile Merkezi Loglama (ELK Stack) Kurulumu](https://medium.com/@deryayildirimm/loglar%C4%B1n-b%C3%BCy%C3%BCs%C3%BCn%C3%BC-ke%C5%9Ffet-spring-boot-docker-ile-merkezi-loglama-elk-stack-kurulumu-84b3e413cf3c)