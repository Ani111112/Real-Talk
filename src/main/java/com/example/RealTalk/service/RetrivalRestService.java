package com.example.RealTalk.service;

import com.example.RealTalk.model.*;
import com.example.RealTalk.repository.CollectionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class RetrivalRestService {
    @Autowired
    CollectionHandler collectionHandler;
    public List<Reference> getAllUser() {
        Optional<List<User>> optionalUsers= Optional.ofNullable(collectionHandler.findAllDocuments(User.class));
        if (!optionalUsers.isPresent()) throw new RuntimeException("You Don't Have Any User");
        List<User> userList = optionalUsers.get();
        List<Reference> referenceList = new ArrayList<>();
        userList.stream().forEach(user -> referenceList.add(getReference(user)));
        return referenceList;
    }
    public Reference getReference(User user) {
        return Reference.builder()
                .id(user.get_id())
                .name(user.getUserName())
                .build();
    }

    public <T> List<MessageInfo> getMessage(T object) {
        ObjectMapper objectMapper = new ObjectMapper();
        SenderAndReciverInfo senderAndReciverInfo = objectMapper.convertValue(object, SenderAndReciverInfo.class);
        String senderId = senderAndReciverInfo.getSenderId();
        String reciverId = senderAndReciverInfo.getReceiverId();
        List<CommunicationInfo> communicationInfos = collectionHandler.findDocumentByField("userInfo._id", senderId, CommunicationInfo.class);
        HashMap<String, List<String>> messageIdMap = communicationInfos.get(0).getMessageHashMap();
        String key = senderId + "_" + reciverId;
        if (!messageIdMap.containsKey(key)) throw new RuntimeException("Start Conversation");
        List<String> idList = messageIdMap.get(key);
        if (idList == null || idList.size() == 0) throw new RuntimeException("No Data Return");
        List<MessageInfo> messageInfoList = collectionHandler.findDocumentsWithFieldQueries("_id", idList, MessageInfo.class);
        if (messageInfoList.size() > 10) {
            List<MessageInfo> messageInfos = new ArrayList<>();
            int start = messageInfoList.size() - 1;
            int end = start - 9;
            for (int i = start; i <= end; i++) {
                messageInfos.add(messageInfoList.get(i));
            }
            return messageInfos;
        }else {
            return messageInfoList;
        }
    }
}
