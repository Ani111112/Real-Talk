package com.example.RealTalk.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "communication_info")
public class CommunicationInfo extends BaseEntity{
    private Reference userInfo;
    private HashMap<String, List<String>> messageHashMap;
}