//package com.vvlanding.testFireBase.controller;
//
//import com.vvlanding.testFireBase.dto.PushNotificationRequest;
//import com.vvlanding.testFireBase.dto.PushNotificationResponse;
//import com.vvlanding.testFireBase.service.PushNotificationService;
//import com.vvlanding.service.SerFirebase;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class PushNotificationController {
//    private final PushNotificationService pushNotificationService;
//
//    public PushNotificationController(PushNotificationService pushNotificationService,SerFirebase serFirebase) {
//        this.pushNotificationService = pushNotificationService;
//    }
//
//    @PostMapping("/notification/token")
//    public ResponseEntity<?> sendTokenNotification(@RequestBody PushNotificationRequest request) {
//        pushNotificationService.sendPushNotificationToToken(request);
//        return new ResponseEntity<>(new PushNotificationResponse(HttpStatus.OK.value(), "Notification has been sent."), HttpStatus.OK);
//    }
//}
