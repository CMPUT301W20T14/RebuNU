package com.example.rebunu;

/**
 * Location class
 * @author Lefan Wang
 * @see Request
 */

public class Location {
    private Double longitude;
    private Double latitude;

    /**
     *
     * @param longitude a double，with the range (0,90)
     * @param latitude a double, with the range (0,180)
     * @throws Exception null or empty or invalid number exceptions
     * @see Request
     */

    public Location(Double longitude , Double latitude) throws Exception {
        setLongitude(longitude);
        setLatitude(latitude);
    }

    /**
     * Getters for all the params
     */

    public Double getLongitude() {
        return longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    /**
     * setter for setLongtitude
     * @param longitude a double >= 0 and <= 180
     * @throws Exception a double >= 0 and <= 180
     */
    public void setLongitude(Double longitude) throws Exception{
        if (longitude == null){
            throw new NullPointerException("Longitude is null");
        }
        else{
            if (longitude >= 0 & longitude <= 180){
                this.longitude = longitude;
            }
            else{
                throw new Exception("Invalid longitude");
            }
        }

    }

    /**
     * setter for setLongtitude
     * @param latitude a double >= 0 and <= 90
     * @throws Exception a double >= 0 and <= 90
     */
    public void setLatitude(Double latitude) throws Exception{
        if (latitude == null){
            throw new NullPointerException("Latitude is null");
        }
        else{
            if (latitude >= 0 & latitude <= 90){
                this.latitude = latitude;
            }
            else{
                throw new Exception("Invalid latitude");
            }
        }

    }
}
