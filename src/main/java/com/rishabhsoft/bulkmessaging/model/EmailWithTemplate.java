package com.rishabhsoft.bulkmessaging.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailWithTemplate {

    private String sender;

    private String receiver;

    private String templateName;

    private String defaultTemplateData;
}
