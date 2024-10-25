package com.xiang.multipurposeTracker.controller;

import com.xiang.multipurposeTracker.DTO.ContactRequest;
import com.xiang.multipurposeTracker.service.ContactRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/httpTriggerContacts")
public class ContactRequestController {

    @Autowired
    private ContactRequestService contactRequestService;

    @PostMapping
    public ResponseEntity<Void> handleContactRequest(@RequestBody ContactRequest contactRequest) {

        System.out.println("Received contact request: " + contactRequest);
        contactRequestService.handleContactRequest(contactRequest);
        return ResponseEntity.ok().build();
    }
}
