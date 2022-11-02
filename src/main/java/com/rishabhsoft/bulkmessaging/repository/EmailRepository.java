package com.rishabhsoft.bulkmessaging.repository;

import com.rishabhsoft.bulkmessaging.model.BulkEmailWithTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmailRepository extends JpaRepository<BulkEmailWithTemplate, Long> {


//    @Query(value="Select s.id, s.date, s.default_template_data, s.sender, s.template_name, t.to_addresses, t.bulk_email_with_template_id from bulk_email_with_template s inner join bulk_email_with_template_to_addresses t on s.id=t.bulk_email_with_template_id order by s.date desc", nativeQuery=true)
   // @Query(value= "Select * from bulk_email_with_template s inner join (select * from bulk_email_with_template_to_addresses order by to_addresses)bulk_email_with_template_to_addresses t on s.id=t.bulk_email_with_template_id order by s.date desc", nativeQuery=true)
    @Query(value ="Select * from bulk_email_with_template order by date desc",nativeQuery=true)
    List<BulkEmailWithTemplate> findAll();

}
