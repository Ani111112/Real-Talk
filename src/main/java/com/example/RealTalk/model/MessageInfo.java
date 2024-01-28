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
@Document(collection = "message_info")
public class MessageInfo extends BaseEntity{
    private String message;
    private Date date;
    private String senderId;
    private String receiverId;
}
