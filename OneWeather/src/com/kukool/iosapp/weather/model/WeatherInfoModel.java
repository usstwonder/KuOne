package com.kukool.iosapp.weather.model;

public class WeatherInfoModel {

}

/**
 * 2011-5-6
 * 当前天气
 */
class CurrentWeatherInfo {
	String weathericon, temperature, weathertext;
    String humidity, realfeel, windspeed, winddirection, visibility;

    public String getWeathertext() {
        return weathertext;
    }

    public void setWeathertext(String weathertext) {
        this.weathertext = weathertext;
    }

    public String getWeathericon() {
		return weathericon;
	}

	public void setWeathericon(String weathericon) {
		this.weathericon = weathericon;
	}

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getRealfeel() {
        return realfeel;
    }

    public void setRealfeel(String realfeel) {
        this.realfeel = realfeel;
    }

    public String getWindspeed() {
        return windspeed;
    }

    public void setWindspeed(String windspeed) {
        this.windspeed = windspeed;
    }

    public String getWinddirection() {
        return winddirection;
    }

    public void setWinddirection(String winddirection) {
        this.winddirection = winddirection;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }


}

/**
 * 2011-5-6
 *第一天到第六天
 */
class WeatherInfoDay1ToDay6 {
	String daycode, weathericon, hightemperature, lowtemperature, weathertext;

    public String getWeathertext() {
        return weathertext;
    }

    public void setWeathertext(String weathertext) {
        this.weathertext = weathertext;
    }

    public String getDaycode() {
		return daycode;
	}

	public void setDaycode(String daycode) {
		this.daycode = daycode;
	}

	public String getWeathericon() {
		return weathericon;
	}

	public void setWeathericon(String weathericon) {
		this.weathericon = weathericon;
	}

	public String getHightemperature() {
		return hightemperature;
	}

	public void setHightemperature(String hightemperature) {
		this.hightemperature = hightemperature;
	}

	public String getLowtemperature() {
		return lowtemperature;
	}

	public void setLowtemperature(String lowtemperature) {
		this.lowtemperature = lowtemperature;
	}
}

