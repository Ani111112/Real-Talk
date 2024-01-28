package com.example.RealTalk.controller;

import com.example.RealTalk.model.MessageInfo;
import com.example.RealTalk.model.Reference;
import com.example.RealTalk.service.RetrivalRestService;
import com.example.RealTalk.service.SaveRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class Controller {
    @Autowired
    SaveRestService saveRestService;
    @Autowired
    RetrivalRestService retrivalRestService;
    @PostMapping("/save/signUp")
    public <T>ResponseEntity signUp(@RequestBody T object) {
        ResponseEntity response = null;
        try {
            T savedObject = saveRestService.signUp(object);
            response = new ResponseEntity<>("Sign Up Successful", HttpStatus.ACCEPTED);
        }catch (Exception exception) {
            response = new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return response;
    }
    @GetMapping("/save/signin")
    public <T>ResponseEntity signin(@RequestParam String mobNo, @RequestParam String password) {
        ResponseEntity response = null;
        try {
            String object = saveRestService.login(mobNo, password);
            response = new ResponseEntity<>(object, HttpStatus.ACCEPTED);
        } catch (Exception exception) {
            response = new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    @GetMapping("/retrival/all-user")
    public ResponseEntity getAllUser() {
        List<Reference> referenceList = null;
        referenceList = retrivalRestService.getAllUser();
        if (referenceList.size() > 0) {
            return new ResponseEntity<>(referenceList, HttpStatus.ACCEPTED);
        }else {
            return new ResponseEntity<>("No Data Return", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/save/message")
    public <T>ResponseEntity saveMessage(@RequestBody T object) {
        String message = saveRestService.saveMessage(object);
        return new ResponseEntity<>(message, HttpStatus.ACCEPTED);
    }

    @GetMapping("/retrival/get-message")
    public <T>ResponseEntity getMessages(@RequestParam("rid") T reciverId, @RequestBody T object) {
        ResponseEntity responseEntity = null;
        try {
            List<MessageInfo> userMessages = retrivalRestService.getMessage(reciverId, object);
            responseEntity = new ResponseEntity<>(userMessages, HttpStatus.ACCEPTED);
        }catch (Exception exception) {
            responseEntity = new ResponseEntity<>("No Data Return", HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }
}
