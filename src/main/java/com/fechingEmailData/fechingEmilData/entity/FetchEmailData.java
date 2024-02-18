package com.fechingEmailData.fechingEmilData.entity;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collation = "fetchData")
public class FetchEmailData {

    @Id
    private String id;
    private String subject;
    private String dateTimeFormat;
    private String fromAddress;
    private String toAddress;



}
