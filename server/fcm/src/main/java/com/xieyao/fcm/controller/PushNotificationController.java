package com.xieyao.fcm.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xieyao.fcm.db.DBHelper;
import com.xieyao.fcm.model.Response;
import com.xieyao.fcm.model.pushnotification.PushNotificationRequest;
import com.xieyao.fcm.model.pushnotification.PushNotificationResponse;
import com.xieyao.fcm.service.PushNotificationService;

@RestController
public class PushNotificationController {

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();
	private Logger logger = LoggerFactory.getLogger(PushNotificationController.class);

	@Value("${app.db}")
	private String dbPath;

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
	@GetMapping("/test")
	public Response test(@RequestParam(value = "name", defaultValue = "World") String name) {
		return new Response((int) counter.incrementAndGet(), String.format(template, name));
	}

	@RequestMapping("/db")
	public String createDatabase() {
//		DBHelper.getInstance().testDb();
		DBHelper.testDb();
		return "Sqlite3 database is created!";
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
