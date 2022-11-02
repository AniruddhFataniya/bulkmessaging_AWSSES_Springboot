package com.rishabhsoft.bulkmessaging.repository;

import com.rishabhsoft.bulkmessaging.model.EmailTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmailTemplateRepository extends JpaRepository<EmailTemplate, Long> {

    void deleteByTemplateName(String templateName);

    @Query(value ="Select * from email_template order by creation_date desc",nativeQuery=true)
    List<EmailTemplate> findAllOrderByCreationDateDesc();

//    @Query(value="Select count(*) from email_template", nativeQuery = true)
//    Long count();

}
