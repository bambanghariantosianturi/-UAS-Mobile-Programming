package com.bambang.uas_bambanghariantosianturi_41514110029;

public class location {
    private String id;
    private String location_name;
    private String address;
    private String longitude;
    private String latitude;

    public location(String id, String location_name, String address, String longitude, String latitude) {
        super();
        this.id = id;
        this.location_name  = location_name;
        this.address        = address;
        this.longitude      = longitude;
        this.latitude       = latitude;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getLocation_name() {
        return location_name;
    }
    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}
