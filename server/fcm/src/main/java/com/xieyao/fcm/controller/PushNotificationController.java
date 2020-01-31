package com.xieyao.fcm.controller;

import com.xieyao.fcm.model.Greeting;
import com.xieyao.fcm.model.PushNotificationRequest;
import com.xieyao.fcm.model.PushNotificationResponse;
import com.xieyao.fcm.service.PushNotificationService;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PushNotificationController {

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

	@Autowired
	private PushNotificationService pushNotificationService;

	public void setPushNotificationService(PushNotificationService pushNotificationService) {
		this.pushNotificationService = pushNotificationService;
	}

	@Autowired
	public PushNotificationController(PushNotificationService pushNotificationService) {
		this.pushNotificationService = pushNotificationService;
	}

	@RequestMapping("/")
	public String index() {
		return "Greetings from Spring Boot!";
	}

	// https://spring.io/guides/gs/rest-service/
	@GetMapping("/greeting")
	public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
		return new Greeting(counter.incrementAndGet(), String.format(template, name));
	}

	@PostMapping("/notification/topic")
	public ResponseEntity sendNotification(@RequestBody PushNotificationRequest request) {
		pushNotificationService.sendPushNotificationWithoutData(request);
		return new ResponseEntity<>(new PushNotificationResponse(HttpStatus.OK.value(), "Notification has been sent."),
				HttpStatus.OK);
	}
	
	@PostMapping("/notification/topic/batch")
	public ResponseEntity sendBatchNotification(@RequestBody List<PushNotificationRequest> requests) {
		pushNotificationService.sendBatchPushNotificationWithoutData(requests);
		return new ResponseEntity<>(new PushNotificationResponse(HttpStatus.OK.value(), "Notification has been sent."),
				HttpStatus.OK);
	}

	@PostMapping("/notification/token")
	public ResponseEntity sendTokenNotification(@RequestBody PushNotificationRequest request) {
		pushNotificationService.sendPushNotificationToToken(request);
		return new ResponseEntity<>(new PushNotificationResponse(HttpStatus.OK.value(), "Notification has been sent."),
				HttpStatus.OK);
	}

	@PostMapping("/notification/data")
	public ResponseEntity sendDataNotification(@RequestBody PushNotificationRequest request) {
		pushNotificationService.sendPushNotification(request);
		return new ResponseEntity<>(new PushNotificationResponse(HttpStatus.OK.value(), "Notification has been sent."),
				HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/notification")
	public ResponseEntity sendSampleNotification() {
		pushNotificationService.sendSamplePushNotification();
		return new ResponseEntity<>(new PushNotificationResponse(HttpStatus.OK.value(), "Notification has been sent."),
				HttpStatus.OK);
	}
}
