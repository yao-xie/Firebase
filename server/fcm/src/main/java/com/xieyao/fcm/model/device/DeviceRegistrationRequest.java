package com.xieyao.fcm.model.device;

import com.xieyao.fcm.model.Request;

public class DeviceRegistrationRequest extends Request {

	private String deviceId;
	private String token;

	public DeviceRegistrationRequest() {
		
	}
	
	public DeviceRegistrationRequest(String deviceId, String token) {
		this.deviceId = deviceId;
		this.token = token;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
