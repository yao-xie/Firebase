package com.xieyao.fcm.firebase;

import java.io.FileInputStream;

import javax.annotation.PostConstruct;

import org.apache.http.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.collect.Lists;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@Service
public class FCMInitializer {

	@Value("${app.firebase-configuration-file}")
	private String firebaseConfigPath;

	public static AccessToken token;

	Logger logger = LoggerFactory.getLogger(FCMInitializer.class);

	@PostConstruct
	public void initialize() {
		print("Firebase call initialize");
		try {
			// init FirebaseApp
			FirebaseOptions options = new FirebaseOptions.Builder().setCredentials(GoogleCredentials.fromStream(
					// new ClassPathResource(firebaseConfigPath).getInputStream()
					new FileInputStream(firebaseConfigPath))).build();
			if (FirebaseApp.getApps().isEmpty()) {
				print("FirebaseApp is empty");
				FirebaseApp app = FirebaseApp.initializeApp(options);
				print("Firebase application has been initialized");
				print("FirebaseApp.getName() = " + app.getName());
			} else {
				print("FirebaseApp is not empty");
			}

			// refresh token
			GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(firebaseConfigPath))
					.createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
			credentials.refreshIfExpired();
			token = credentials.refreshAccessToken();
			print(String.format("token is %s",
					null == token || TextUtils.isEmpty(token.getTokenValue()) ? "null" : token.getTokenValue()));
		} catch (Exception e) {
			print("Firebase initialize catach error message: " + e.getMessage());
		}
	}

	public void print(String text) {
		System.out.print(text);
		logger.info(text);
	}

}
