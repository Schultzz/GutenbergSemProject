package data.dto;

/**
 * Created by Soren on 15-05-2017.
 */
public class CityDTO {

    private String cityName;
    private double longtitude;
    private double latitude;

    public CityDTO(String cityName, double longtitude, double latitude) {
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

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
