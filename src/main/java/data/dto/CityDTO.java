package data.dto;

/**
 * Created by Soren on 15-05-2017.
 */
public class CityDTO {

    private String cityName;
    private float longtitude;
    private float latitude;

    public CityDTO(String cityName, float longtitude, float latitude) {
        this.cityName = cityName;
        this.longtitude = longtitude;
        this.latitude = latitude;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public float getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(float longtitude) {
        this.longtitude = longtitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }
}
