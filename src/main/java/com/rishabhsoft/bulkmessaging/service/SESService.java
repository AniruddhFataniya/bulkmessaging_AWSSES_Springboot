package com.rishabhsoft.bulkmessaging.service;

import com.rishabhsoft.bulkmessaging.model.BulkEmailWithTemplate;
import com.rishabhsoft.bulkmessaging.model.Email;
import com.rishabhsoft.bulkmessaging.model.EmailTemplate;
import com.rishabhsoft.bulkmessaging.model.EmailWithTemplate;
import software.amazon.awssdk.services.ses.SesClient;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

public interface SESService {

    void createIdentity(SesClient client, String identity);

    void deleteIdentity(SesClient client, String identity);

    List<String> getIdentity(SesClient client) throws IOException;

    Boolean searchIdentity(String identity, SesClient client) throws IOException;

    void sendEmail(SesClient client, Email email) throws MessagingException;

    void sendBulkEmail(SesClient client, BulkEmailWithTemplate bulkEmailWithTemplate);

    void saveBulkEmail(BulkEmailWithTemplate bulkEmailWithTemplate);

    void sendEmailWithTemplate(SesClient client, EmailWithTemplate emailWithTemplate) throws MessagingException;

    void createTemplate(SesClient client, EmailTemplate template);

    void saveTemplate(EmailTemplate template);

    void deleteTemplate(SesClient client, String templateName);

    void deleteTemplateFromDb(String templateName);

    List<String> listTemplate(SesClient client);

    List<EmailTemplate> listTemplateFromDb();

    Boolean searchTemplate(SesClient client, String templateName);

    List<BulkEmailWithTemplate> getEmailHistory();

    void clearEmailHistory();

    Long templateCount();



}
