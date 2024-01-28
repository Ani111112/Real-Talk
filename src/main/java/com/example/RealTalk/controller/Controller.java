package com.example.RealTalk.controller;

import com.example.RealTalk.model.MessageInfo;
import com.example.RealTalk.model.Reference;
import com.example.RealTalk.model.User;
import com.example.RealTalk.service.RetrivalRestService;
import com.example.RealTalk.service.SaveRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5500")
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
    @PostMapping("/save/signin")
    public <T>ResponseEntity signin(@RequestBody T object) {
        ResponseEntity response = null;
        try {
            User savedUser = (User) saveRestService.login(object);
            response = new ResponseEntity<>(savedUser, HttpStatus.ACCEPTED);
        } catch (Exception exception) {
            response = new ResponseEntity<>(exception.getMessage(), HttpStatus.FORBIDDEN);
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

    @PostMapping("/retrival/get-message")
    public <T>ResponseEntity getMessages(@RequestBody T object) {
        ResponseEntity responseEntity = null;
        try {
            List<MessageInfo> userMessages = retrivalRestService.getMessage(object);
            responseEntity = new ResponseEntity<>(userMessages, HttpStatus.ACCEPTED);
        }catch (Exception exception) {
            responseEntity = new ResponseEntity<>("No Data Return", HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }
}
