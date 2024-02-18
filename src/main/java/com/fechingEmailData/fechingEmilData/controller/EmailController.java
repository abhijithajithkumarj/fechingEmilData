package com.fechingEmailData.fechingEmilData.controller;

import com.fechingEmailData.fechingEmilData.service.FetchingService;
import com.fechingEmailData.fechingEmilData.serviceImp.FetchingEmailServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/url")
public class EmailController {


    @Autowired
    private FetchingService fetchingEmailService;


    @PutMapping("/read")
    public ResponseEntity<String> readingEmail() {
        boolean result = fetchingEmailService.fetchUnreadMessages();
        if (result) {
            return ResponseEntity.ok("Mail read completed.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("something went wrong.");
        }
    }



}
