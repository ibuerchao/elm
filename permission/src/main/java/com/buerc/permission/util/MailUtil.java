package com.buerc.permission.util;

import com.buerc.sys.dto.MailParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

/**
 * 邮件工具类
 */
@Slf4j
@Service
public class MailUtil {
    @Resource
    private TemplateEngine templateEngine;

    @Resource
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String from;

    /**
     * 纯文本邮件
     */
    @Async("mailThreadPool")
    public void sendTextMail(MailParam mail) {
        //建立邮件消息
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from); // 发送人的邮箱
        message.setSubject(mail.getTitle()); //标题
        message.setTo(mail.getEmail()); //发给谁  对方邮箱
        message.setText(mail.getContent()); //内容
        try {
            javaMailSender.send(message); //发送
        } catch (MailException e) {
            log.error("纯文本邮件发送失败->message:{}", e.getMessage());
        }
    }

    /**
     * 发送的邮件是富文本（附件，图片，html等）
     */
    @Async("mailThreadPool")
    public void sendHtmlMail(MailParam mail, boolean isShowHtml) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            //是否发送的邮件是富文本（附件，图片，html等）
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setFrom(from);// 发送人的邮箱
            messageHelper.setTo(mail.getEmail());//发给谁  对方邮箱
            messageHelper.setSubject(mail.getTitle());//标题
            messageHelper.setText(mail.getContent(), isShowHtml);//false，显示原始html代码，无效果
            //判断是否有附加图片等
            if (MapUtils.isNotEmpty(mail.getAttachment())) {
                mail.getAttachment().forEach((key, value) -> {
                    try {
                        File file = new File(String.valueOf(value));
                        if (file.exists()) {
                            messageHelper.addAttachment(key, new FileSystemResource(file));
                        }
                    } catch (MessagingException e) {
                        log.error("附件发送失败->message:{}", e.getMessage());
                    }
                });
            }
            //发送
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("富文本邮件发送失败->message:{}", e.getMessage());
        }
    }

    /**
     * 发送模板邮件 使用thymeleaf模板
     * 若果使用freemarker模板
     * Configuration configuration = new Configuration(Configuration.VERSION_2_3_28);
     * configuration.setClassForTemplateLoading(this.getClass(), "/templates");
     * String emailContent = FreeMarkerTemplateUtils.processTemplateIntoString(configuration.getTemplate("mail.ftl"), params);
     *
     */
    @Async("mailThreadPool")
    public void sendTemplateMail(MailParam mail,String templateName) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setFrom(from);// 发送人的邮箱
            messageHelper.setTo(mail.getEmail());//发给谁  对方邮箱
            messageHelper.setSubject(mail.getTitle()); //标题
            //使用模板thymeleaf
            Context context = new Context();
            //定义模板数据
            context.setVariables(mail.getAttachment());
            //获取thymeleaf的html模板
            String emailContent = templateEngine.process(templateName, context); //指定模板路径
            messageHelper.setText(emailContent, true);
            //发送邮件
            javaMailSender.send(mimeMessage);
            log.info("模板邮件发送成功->receiver:{}",mail.getEmail());
        } catch (Exception e) {
            log.error("模板邮件发送失败->message:{}", e.getMessage());
        }
    }
}
