package com.rishabhsoft.bulkmessaging.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Email {

    private String sender;

    private String receiver;

    private String subject;

    private String emailBody;
}
