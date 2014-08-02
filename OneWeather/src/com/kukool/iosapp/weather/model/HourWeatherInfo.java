package com.kukool.iosapp.weather.model;

public class HourWeatherInfo {

	String time, value;
	public HourWeatherInfo(String time, String value) {
		this.time = time;
		this.value = value; 
	}

	public String getTime() {
		return time;
	}
	public String getValue() {
		return value;
	}

}
