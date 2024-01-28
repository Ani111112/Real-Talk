package com.example.RealTalk.service;

import com.example.RealTalk.model.CommunicationInfo;
import com.example.RealTalk.model.MessageInfo;
import com.example.RealTalk.model.Reference;
import com.example.RealTalk.model.User;
import com.example.RealTalk.repository.CollectionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SaveRestService {
    @Autowired
    CollectionHandler collectionHandler;

    public <T> T signUp(T object) {
        ObjectMapper objectMapper = new ObjectMapper();
        User user = objectMapper.convertValue(object, User.class);
        String userMobileNumber = user.getMobNo();
        List<User> savedUser = collectionHandler.findDocumentByField("mobNo", userMobileNumber, User.class);
        if (savedUser != null && savedUser.size() > 0) throw new RuntimeException("You Already Have An Account Try to Login");
        return (T) collectionHandler.save(user);
    }

    public <T> T login(String mobNo, String password) {
        List<User> savedUser = collectionHandler.findDocumentByField("mobNo", mobNo, User.class);
        if (savedUser == null || savedUser.size() == 0) throw new RuntimeException("You Don't Have Any Account In this Number Try To Sign Up");
        String oldPassWord = savedUser.get(0).getPassword();
        if (!oldPassWord.equals(password)) throw new RuntimeException("You Entered Wrong Password");
        return (T) "Sign in Successful";
    }

    public String saveMessage(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        MessageInfo messageInfo = objectMapper.convertValue(object, MessageInfo.class);
        String senderId = messageInfo.getSenderId();
        String receiverId = messageInfo.getReceiverId();
        List<CommunicationInfo> communicationInfoForSender = collectionHandler.findDocumentByField("userInfo._id", senderId, CommunicationInfo.class);
        List<CommunicationInfo> communicationInfoForReciver = collectionHandler.findDocumentByField("userInfo._id", receiverId, CommunicationInfo.class);
        messageInfo.setDate(new Date());
        MessageInfo message = (MessageInfo) collectionHandler.save(messageInfo);
        List<User> senderUser = collectionHandler.findDocumentByField("_id", senderId, User.class);
        List<User> reciveUser = collectionHandler.findDocumentByField("_id", receiverId, User.class);
        saveCommunicationInfo(communicationInfoForSender, message, senderId, receiverId, senderId, senderUser);
        saveCommunicationInfo(communicationInfoForReciver, message, senderId, receiverId, receiverId, reciveUser);
        return "Message Saved Successfully";
    }
    private void saveCommunicationInfo(List<CommunicationInfo> CommunicationInfos, MessageInfo message, String senderId, String receiverId, String referenceId, List<User>users) {
        if (CommunicationInfos != null && CommunicationInfos.size() > 0) {
            CommunicationInfo communication = CommunicationInfos.get(0);
            HashMap<String, List<String>> communicationInfo = communication.getMessageHashMap();
            String key = senderId + "_" + receiverId;
            if (!communicationInfo.containsKey(key)) {
                List<String> messageIdList = new ArrayList<>();
                messageIdList.add(message.get_id());
                communicationInfo.put(key, messageIdList);
            }else {
                List<String> messageIdList = communicationInfo.get(key);
                messageIdList.add(message.get_id());
                communicationInfo.put(key, messageIdList);
            }
            collectionHandler.save(communication);
        }else {
            CommunicationInfo communication = new CommunicationInfo();
            HashMap<String, List<String>> communicationInfo = new HashMap<>();
            List<String> messageIdList = new ArrayList<>();
            String key = senderId + "_" + receiverId;
            messageIdList.add(message.get_id());
            communicationInfo.put(key, messageIdList);
            communication.setMessageHashMap(communicationInfo);
            Reference userInfo = new Reference();
            userInfo.setId(referenceId);
            userInfo.setName(users.get(0).getUserName());
            communication.setUserInfo(userInfo);
            collectionHandler.save(communication);
        }
    }
}
