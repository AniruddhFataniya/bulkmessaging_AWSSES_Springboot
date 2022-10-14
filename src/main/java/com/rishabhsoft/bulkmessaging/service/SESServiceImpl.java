package com.rishabhsoft.bulkmessaging.service;

import com.rishabhsoft.bulkmessaging.model.BulkEmailWithTemplate;
import com.rishabhsoft.bulkmessaging.model.Email;
import com.rishabhsoft.bulkmessaging.model.EmailTemplate;
import com.rishabhsoft.bulkmessaging.model.EmailWithTemplate;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;
import software.amazon.awssdk.services.ses.model.Destination;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SESServiceImpl implements SESService{


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

        List<BulkEmailDestination> bulkEmailDestinationListTo = new ArrayList<>();
        List<BulkEmailDestination>bulkEmailDestinationListCc = new ArrayList<>();
        List<BulkEmailDestination>bulkEmailDestinationListBcc = new ArrayList<>();
       // SendBulkTemplatedEmailRequest sendBulkTemplatedEmailRequest = null;
    try{
        // to addresses
        for(String destTo: bulkEmailWithTemplate.getToAddresses()){
            Destination destinationTo = Destination.builder()
                    .toAddresses(destTo)
                    .build();

           BulkEmailDestination bulkEmailDestinationTo = BulkEmailDestination.builder()
                    .destination(destinationTo)
                    .build();

            bulkEmailDestinationListTo.add(bulkEmailDestinationTo);

        }

        // cc addresses
        for(String destCC: bulkEmailWithTemplate.getCcAddresses()){
            Destination destinationCC = Destination.builder()
                    .toAddresses(destCC)
                    .build();

            BulkEmailDestination bulkEmailDestinationCc = BulkEmailDestination.builder()
                    .destination(destinationCC)
                    .build();

            bulkEmailDestinationListCc.add(bulkEmailDestinationCc);

        }

        // bcc addresses
        for(String destBcc: bulkEmailWithTemplate.getCcAddresses()){
            Destination destinationBcc = Destination.builder()
                    .toAddresses(destBcc)
                    .build();

            BulkEmailDestination bulkEmailDestinationBcc = BulkEmailDestination.builder()
                    .destination(destinationBcc)
                    .build();

            bulkEmailDestinationListBcc.add(bulkEmailDestinationBcc);

        }


        SendBulkTemplatedEmailRequest sendBulkTemplatedEmailRequest = SendBulkTemplatedEmailRequest.builder()
                .source(bulkEmailWithTemplate.getSender())
                .template(bulkEmailWithTemplate.getTemplateName())
                .destinations(bulkEmailDestinationListTo)
                .destinations(bulkEmailDestinationListCc)
                .destinations(bulkEmailDestinationListBcc)
                .defaultTemplateData(bulkEmailWithTemplate.getDefaultTemplateData())
                .build();

        client.sendBulkTemplatedEmail(sendBulkTemplatedEmailRequest);
        System.out.println(">>sender:"+bulkEmailWithTemplate.getSender());

    }catch(Exception e){
        System.out.println("The email was not sent. Error message: " + e.getMessage());
        e.printStackTrace();
    }


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
    public void deleteTemplate(SesClient client, String templateName) {

        DeleteTemplateRequest deleteTemplateRequest = DeleteTemplateRequest.builder()
                .templateName(templateName)
                .build();

        client.deleteTemplate(deleteTemplateRequest);
    }

    @Override
    public List<String> listTemplate(SesClient client) {

        List<String>templateList = new ArrayList<>();
        ListTemplatesRequest listTemplatesRequest = ListTemplatesRequest.builder()
                .maxItems(30)
                .build();

        ListTemplatesResponse listTemplatesResponse = client.listTemplates(listTemplatesRequest);
         listTemplatesResponse.templatesMetadata().forEach(template -> templateList.add(template.name()));

        return templateList;
    }
}
