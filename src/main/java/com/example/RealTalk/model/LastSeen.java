package com.example.RealTalk.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Document(collection = "lastseen")
public class LastSeen extends BaseEntity{
    private Reference user;
    private Date date;
}
