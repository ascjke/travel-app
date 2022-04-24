package ru.edu.service;

public class CityInfo implements Comparable<CityInfo> {

    private String id;
    private String name;
    private String description;
    private String climate;
    private int population;
    private double latitude;  // radians
    private double longitude; // radians

    public CityInfo() {
    }

    public CityInfo(String id, String name, String description, String climate, int population, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.climate = climate;
        this.population = population;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getClimate() {
        return climate;
    }

    public void setClimate(String climate) {
        this.climate = climate;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }



    @Override
    public String toString() {
        return "CityInfo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", climate='" + climate + '\'' +
                ", population=" + population +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    @Override
    public int compareTo(CityInfo o) {

        return this.getName().compareTo(o.getName());
    }
}