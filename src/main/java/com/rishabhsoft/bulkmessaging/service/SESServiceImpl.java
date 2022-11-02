package com.rishabhsoft.bulkmessaging.service;

import com.rishabhsoft.bulkmessaging.model.BulkEmailWithTemplate;
import com.rishabhsoft.bulkmessaging.model.Email;
import com.rishabhsoft.bulkmessaging.model.EmailTemplate;
import com.rishabhsoft.bulkmessaging.model.EmailWithTemplate;
import com.rishabhsoft.bulkmessaging.repository.EmailRepository;
import com.rishabhsoft.bulkmessaging.repository.EmailTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;
import software.amazon.awssdk.services.ses.model.Destination;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
@Transactional
public class SESServiceImpl implements SESService{

    @Autowired
    EmailRepository emailRepository;

    @Autowired
    EmailTemplateRepository emailTemplateRepository;

    EmailTemplate emailTemplate;

    @Override
    public void createIdentity(SesClient client, String identity) {

        VerifyEmailIdentityRequest verifyEmailIdentityRequest = VerifyEmailIdentityRequest.builder()
                .emailAddress(identity)
                .build();

        client.verifyEmailIdentity(verifyEmailIdentityRequest);
    }

    @Override
    public void deleteIdentity(SesClient client, String identity) {
        DeleteIdentityRequest deleteIdentityRequest = DeleteIdentityRequest.builder()
                .identity(identity)
                .build();

        client.deleteIdentity(deleteIdentityRequest);
    }

    @Override
    public List<String> getIdentity(SesClient client) throws IOException {

        try {
            ListIdentitiesResponse identitiesResponse = client.listIdentities();
            List<String> identities = identitiesResponse.identities();

            return identities;
        } catch (SesException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            return null;
        }

    }

    @Override
    public Boolean searchIdentity(String identity, SesClient client) throws IOException {
        List<String> identities;
        identities = getIdentity(client);
        int flag = 0;

        for(String id: identities){
            if(id.equals(identity)){
                flag = 1;
                break;
            }
        }
        if(flag == 1)
        {
            return true;
        }
        else{
            return false;
        }


    }

    @Override
    public void sendEmail(SesClient client, Email email) throws MessagingException {

        Destination destination = Destination.builder()
                .toAddresses(email.getReceiver())
                .build();

        Content content = Content.builder()
                .data(email.getEmailBody())
                .build();

        Content subject = Content.builder()
                .data(email.getSubject())
                .build();

        Body body = Body.builder()
                .html(content)
                .build();

        Message message = Message.builder()
                .subject(subject)
                .body(body)
                .build();

        SendEmailRequest emailRequest = SendEmailRequest.builder()
                .message(message)
                .source(email.getSender())
                .destination(destination)
                .build();

        try {
            System.out.println("Attempting to send an email through Amazon SES " + "using the AWS SDK for Java...");
            System.out.println("Sender:"+email.getSender());
            System.out.println("Receiver:"+ destination);
            client.sendEmail(emailRequest);

        } catch (SesException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }


    }

    @Override
    public void sendBulkEmail(SesClient client, BulkEmailWithTemplate bulkEmailWithTemplate) {

        String receiverName = bulkEmailWithTemplate.getName();
        if(receiverName== null){
            receiverName = "customer";
        }


        List<BulkEmailDestination> bulkEmailDestinationListTo = new ArrayList<>();
//        List<BulkEmailDestination>bulkEmailDestinationListCc = new ArrayList<>();
//        List<BulkEmailDestination>bulkEmailDestinationListBcc = new ArrayList<>();
       // SendBulkTemplatedEmailRequest sendBulkTemplatedEmailRequest = null;
    try{

        String[] toAdd = StringUtils.commaDelimitedListToStringArray(bulkEmailWithTemplate.getToAddresses());
        // to addresses
        for(String destTo: toAdd){
            Destination destinationTo = Destination.builder()
                    .toAddresses(destTo)
                    .build();

           BulkEmailDestination bulkEmailDestinationTo = BulkEmailDestination.builder()
                    .destination(destinationTo)
                    .build();

            bulkEmailDestinationListTo.add(bulkEmailDestinationTo);

        }

        // cc addresses
      /*  for(String destCC: bulkEmailWithTemplate.getCcAddresses()){
            Destination destinationCC = Destination.builder()
                    .toAddresses(destCC)
                    .build();

            BulkEmailDestination bulkEmailDestinationCc = BulkEmailDestination.builder()
                    .destination(destinationCC)
                    .build();

            bulkEmailDestinationListCc.add(bulkEmailDestinationCc);

        }

        // bcc addresses
        for(String destBcc: bulkEmailWithTemplate.getBccAddresses()){
            Destination destinationBcc = Destination.builder()
                    .toAddresses(destBcc)
                    .build();

            BulkEmailDestination bulkEmailDestinationBcc = BulkEmailDestination.builder()
                    .destination(destinationBcc)
                    .build();

            bulkEmailDestinationListBcc.add(bulkEmailDestinationBcc);

        }*/
    MessageTag messageTag = MessageTag.builder()
            .name("BulkEmail")
            .value("status")
            .build();

        SendBulkTemplatedEmailRequest sendBulkTemplatedEmailRequest = SendBulkTemplatedEmailRequest.builder()
                .source(bulkEmailWithTemplate.getSender())
                .template(bulkEmailWithTemplate.getTemplateName())
                .destinations(bulkEmailDestinationListTo)
                .configurationSetName("bulkEmailConfigCloudWatch")
                .defaultTags(messageTag)
                //.destinations(bulkEmailDestinationListCc)
                //.destinations(bulkEmailDestinationListBcc)

               //.defaultTemplateData(bulkEmailWithTemplate.getDefaultTemplateData())
                 //  .defaultTemplateData("{\"name\":\"friend\"}")
                .defaultTemplateData("{\"name\": "+receiverName+"}")

                .build();

        client.sendBulkTemplatedEmail(sendBulkTemplatedEmailRequest);

        System.out.println(">>sender:"+bulkEmailWithTemplate.getSender());

    }catch(Exception e){
        System.out.println("The email was not sent. Error message: " + e.getMessage());
        e.printStackTrace();
    }


    }

    @Override
    public void saveBulkEmail(BulkEmailWithTemplate bulkEmailWithTemplate) {

        emailRepository.save(bulkEmailWithTemplate);
    }

    @Override
    public void sendEmailWithTemplate(SesClient client, EmailWithTemplate emailWithTemplate) throws MessagingException {

        Destination destination = Destination.builder()
                .toAddresses(emailWithTemplate.getReceiver())
                .build();

        SendTemplatedEmailRequest templatedEmailRequest = SendTemplatedEmailRequest.builder()
                .destination(destination)
                .source(emailWithTemplate.getSender())
                .template(emailWithTemplate.getTemplateName())
                .templateData(emailWithTemplate.getDefaultTemplateData())
                .build();
        try {
            System.out.println(">>sender:"+emailWithTemplate.getSender());
            client.sendTemplatedEmail(templatedEmailRequest);

        }catch (SesException e){

            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }

    }

    @Override
    public void createTemplate(SesClient client, EmailTemplate emailTemplate) {
        String html;

            html = "<html><body>There is no content in the template</body></html>";


        Template template = Template.builder()
                .templateName(emailTemplate.getTemplateName())
                .subjectPart(emailTemplate.getSubjectPart())
                .htmlPart(emailTemplate.getHtmlPart())
                .textPart(emailTemplate.getTextPart())
                .build();

        CreateTemplateRequest createTemplateRequest = CreateTemplateRequest.builder()
                .template(template)
                .build();
        try{
            client.createTemplate(createTemplateRequest);
        } catch(Exception e){
            System.out.println("Error while creating the template:"+ e.getMessage() );
            e.printStackTrace();
        }

    }

    @Override
    public void saveTemplate(EmailTemplate template) {
        emailTemplateRepository.save(template);
    }

    @Override
    public void deleteTemplate(SesClient client, String templateName) {


        DeleteTemplateRequest deleteTemplateRequest = DeleteTemplateRequest.builder()
                .templateName(templateName)
                .build();

        client.deleteTemplate(deleteTemplateRequest);
    }

    @Override
    public void deleteTemplateFromDb(String templateName) {
        emailTemplateRepository.deleteByTemplateName(templateName);
    }

    @Override
    public List<String> listTemplate(SesClient client) {

        List<String>templateList = new ArrayList<>();
        ListTemplatesRequest listTemplatesRequest = ListTemplatesRequest.builder()
                .maxItems(10000)
                .build();

        ListTemplatesResponse listTemplatesResponse = client.listTemplates(listTemplatesRequest);
         listTemplatesResponse.templatesMetadata().forEach(template -> templateList.add(template.name()));

        return templateList;
    }

    @Override
    public List<EmailTemplate> listTemplateFromDb() {
        return emailTemplateRepository.findAllOrderByCreationDateDesc();
    }

    @Override
    public Boolean searchTemplate(SesClient client, String templateName) {

        List<String> templates;
        templates = listTemplate(client);
        int flag = 0;

        for(String template: templates){
            if(template.equals(templateName)){
                flag = 1;
                break;
            }
        }
        if(flag == 1)
        {
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public List<BulkEmailWithTemplate> getEmailHistory() {
        return emailRepository.findAll();
    }

    @Override
    public void clearEmailHistory() {
        emailRepository.deleteAll();
    }

    @Override
    public Long templateCount() {
        Long templateCount;
        Long remainingTemplate;
        templateCount = emailTemplateRepository.count();
        System.out.println("templateCount:"+templateCount);
        remainingTemplate = 10000 - templateCount;
        return remainingTemplate;
    }
}
