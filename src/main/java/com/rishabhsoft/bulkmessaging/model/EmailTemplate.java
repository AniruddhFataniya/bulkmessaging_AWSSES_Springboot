package com.rishabhsoft.bulkmessaging.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailTemplate {

    private Long id;

    private String templateName;

    private String subjectPart;

    private String htmlPart;

    private String textPart;

}
