# 1. Maven ile derleme yapacağımız base image
FROM maven:3.9.6-eclipse-temurin-21 AS builder

# 2. Proje dosyalarını container içine kopyala
WORKDIR /app
COPY . .

# 3. Maven ile uygulamayı build et (testleri atlayarak)
RUN mvn clean package -DskipTests

# 4. Sadece çalışabilir JAR dosyasını kullanacak yeni bir base image
FROM eclipse-temurin:21-jre

# 5. JAR dosyasını çalıştırmak için kopyala
COPY --from=builder /app/target/*.jar app.jar

# 6. Uygulama 5005 gibi bir port dinliyorsa expose edebilirsin (isteğe bağlı)
EXPOSE 8080

# 7. Uygulamayı başlat
ENTRYPOINT ["java", "-jar", "app.jar"]
