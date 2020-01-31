package com.xieyao.fcm.firebase;

import com.google.firebase.messaging.*;
import com.xieyao.fcm.model.PushNotificationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class FCMService {

	private Logger logger = LoggerFactory.getLogger(FCMService.class);

	public void sendMessage(Map<String, String> data, PushNotificationRequest request)
			throws InterruptedException, ExecutionException {
		Message message = getPreconfiguredMessageWithData(data, request);
		String response = sendAndGetResponse(message);
		logger.info("Sent message with data. request = " + request + ", response = " + response);
	}

	public void sendMessageWithoutData(PushNotificationRequest request)
			throws InterruptedException, ExecutionException {
		Message message = getPreconfiguredMessageWithoutData(request);
		String response = sendAndGetResponse(message);
		logger.info("Sent message without data. request = " + request + ", response = " + response);
	}

	public void sendBatchMessageWithoutData(List<PushNotificationRequest> requests)
			throws InterruptedException, ExecutionException {
		List<Message> messages = new ArrayList<>();
		for (PushNotificationRequest request : requests) {
			messages.add(getPreconfiguredMessageWithoutData(request));
		}
		BatchResponse batchResponse = null;
		try {
			batchResponse = sendBatchAndGetResponse(messages);
		} catch (FirebaseMessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("Sent batch messages without data. requests = " + requests + ", response successCount = "
				+ (null != batchResponse ? batchResponse.getSuccessCount() : 0));
	}

	public void sendMessageToToken(PushNotificationRequest request) throws InterruptedException, ExecutionException {
		Message message = getPreconfiguredMessageToToken(request);
		String response = sendAndGetResponse(message);
		logger.info("Sent message to token. request = " + request + ", response = " + response);
	}

	private String sendAndGetResponse(Message message) throws InterruptedException, ExecutionException {
		return FirebaseMessaging.getInstance().sendAsync(message).get();
	}

	private BatchResponse sendBatchAndGetResponse(List<Message> messages)
			throws InterruptedException, ExecutionException, FirebaseMessagingException {
		return FirebaseMessaging.getInstance().sendAll(messages);
	}

	private AndroidConfig getAndroidConfig(String topic) {
		return AndroidConfig.builder().setTtl(Duration.ofMinutes(2).toMillis()).setCollapseKey(topic)
				.setPriority(AndroidConfig.Priority.HIGH)
				.setNotification(AndroidNotification.builder().setSound(NotificationParameter.SOUND.getValue())
						.setColor(NotificationParameter.COLOR.getValue()).setTag(topic).build())
				.build();
	}

	private ApnsConfig getApnsConfig(String topic) {
		return ApnsConfig.builder().setAps(Aps.builder().setCategory(topic).setThreadId(topic).build()).build();
	}

	private Message getPreconfiguredMessageToToken(PushNotificationRequest request) {
		return getPreconfiguredMessageBuilder(request).setToken(request.getToken()).build();
	}

	private Message getPreconfiguredMessageWithoutData(PushNotificationRequest request) {
		return getPreconfiguredMessageBuilder(request).setTopic(request.getTopic()).build();
	}

	private Message getPreconfiguredMessageWithData(Map<String, String> data, PushNotificationRequest request) {
		return getPreconfiguredMessageBuilder(request).putAllData(data).setTopic(request.getTopic()).build();
	}

	@SuppressWarnings("deprecation")
	private Message.Builder getPreconfiguredMessageBuilder(PushNotificationRequest request) {
		AndroidConfig androidConfig = getAndroidConfig(request.getTopic());
		ApnsConfig apnsConfig = getApnsConfig(request.getTopic());
		return Message.builder().setApnsConfig(apnsConfig).setAndroidConfig(androidConfig)
				.setNotification(new Notification(request.getTitle(), request.getMessage()));
	}

}
