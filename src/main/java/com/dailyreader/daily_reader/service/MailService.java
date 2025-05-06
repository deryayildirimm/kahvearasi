package com.dailyreader.daily_reader.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Slf4j
@Service
public class MailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;


    public MailService(JavaMailSender mailSender,
                       TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Retryable(
            maxAttempts = 6,
            retryFor = {MessagingException.class},
            backoff = @Backoff(delay = 5000)
    )
    public void sendEmail(String to, String userName, String contentTitle, String contentBody) {

        try{
            MDC.put("receiver", to);
            MDC.put("userName", userName);
            log.info("📨 Mail gönderimi denemesi başlatılıyor → Alıcı: {}", to);
            Context context = new Context();
            context.setVariable("userName", userName );
            context.setVariable("contentTitle", contentTitle);
            context.setVariable("contentBody", contentBody);

            String htmlContent = templateEngine.process("mail-template", context);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject("Your Daily Content ✉️");
            helper.setText(htmlContent, true); // ikinci parametre: true → HTML format

            log.info("✅ Mail başarıyla gönderildi → {}", to);
            mailSender.send(message);
        }catch (MessagingException exception){
            log.warn("Email gönderimi başarısız");
            log.error("Email gönderimi başarısız oldu", exception);
        }finally {
            MDC.clear();
        }

    }

    @Recover
    public void recover(MessagingException e, String to, String subject, String htmlContent) {
        log.warn("Çok fazla denendi. Başarısız olduk.");
        log.error("❌ Retry limitine ulaşıldı. Mail gönderilemedi → Alıcı: {} | Hata: {}", to, e.getMessage());


    }
}
