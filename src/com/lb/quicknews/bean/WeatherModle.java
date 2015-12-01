package com.lb.quicknews.bean;

public class WeatherModle extends BaseModle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7446128767891324783L;
	private String temprature;
	private String weather;
	private String wind;
	private String week;
	private String date;

	public String getTemprature() {
		return temprature;
	}

	public void setTemprature(String temprature) {
		this.temprature = temprature;
	}

	public String getWeather() {
		return weather;
	}

	public void setWeather(String weather) {
		this.weather = weather;
	}

	public String getWind() {
		return wind;
	}

	public void setWind(String wind) {
		this.wind = wind;
	}

	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		this.week = week;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}
