# Bulk Email Sender
This Application is used to send bulk emails.

### Prerequisite:
* Verify the email by which the emails to be sent with AWS SES 

## Features:
* Creating email template
* Deleting email template
* Sending bulk emails
* Getting list of the template

### 1. Creating an email template

* First, create an HTML file of the template at https://mjml.io/templates.
* Convert it to JSON Escape at https://www.freeformatter.com/json-escape.html#before-output
* Copy the text and put into "htmlPart" and other required fields while hitting the endpoint
* Fields required:
  * templateName: the name to be given to the template
  * subjectPart: subject of the email
  * htmlPart: html template code
  * textPart: text to be display if the template is not loaded
### 2. Deleting an email template
* put the name of the template as a path variable and hit "Delete" method
  
### Sending bulk email
* In the receiver field, mention all the recipients and fill other required fields
* Fields required:
  * sender: email of the sender
  * receiver: emails of the recipients
  * templateName: name of the email template to be used with email
  * defaultTemplateData: pass the data if you have used any variable in the template
  
## Limitations:
* Attachment facility is not available
* Sending email to only verified identities as it is using AWS SES sandbox environment
