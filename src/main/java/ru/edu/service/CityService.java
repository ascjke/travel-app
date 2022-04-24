package ru.edu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.edu.dao.CityRepository;
import ru.edu.error.CustomException;
import ru.edu.error.Errors;
import ru.edu.service.image_and_geo.ImageAndGeoProvider;
import ru.edu.service.weather.WeatherCache;
import ru.edu.service.weather.WeatherInfo;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class CityService {

    private CityRepository repository;
    private WeatherCache weatherCache;
    private ImageAndGeoProvider imageAndGeoProvider;


    @Autowired
    public void setRepository(CityRepository repository) {
        this.repository = repository;
    }

    @Autowired
    public void setImageProvider(ImageAndGeoProvider imageAndGeoProvider) {
        this.imageAndGeoProvider = imageAndGeoProvider;
    }

    @Autowired
    public void setWeatherCache(WeatherCache weatherCache) {
        this.weatherCache = weatherCache;
    }

    public WeatherCache getWeatherCache() {
        return weatherCache;
    }

    /**
     * Get all.
     */
    public List<CityInfo> getAll() {

        return repository.getAll();
    }

    /**
     * Get city by id. Returns null if not found.
     *
     * @param cityId - city id
     * @throws ru.edu.error.CustomException with code CITY_NOT_FOUND if city doesn't exist
     */
    public CityInfo getCityById(String cityId) {

        if (repository.getCityById(cityId) == null) {
            throw new CustomException("Can not getCity() with id=" + cityId, Errors.CITY_NOT_FOUND);
        }
        return repository.getCityById(cityId);
    }

    /**
     * Create new city.
     *
     * @throws ru.edu.error.CustomException with code CITY_ALREADY_EXISTS if city with current id already exists
     * @throws ru.edu.error.CustomException with code ARGUMENT_ERROR if info is incorrect
     */
    public CityInfo create(CityInfo info) {

        for(CityInfo city: repository.getAll()) {
            if (info.getName().equalsIgnoreCase(city.getName())) {
                throw new CustomException("Can not create() city with name=" + info.getName(),
                        Errors.CITY_ALREADY_EXISTS);
            }
        }

        if (isIncorrectCityInfo(info)) {
            throw new CustomException("Can not create() ERROR", Errors.ARGUMENT_ERROR);
        }

        return repository.create(info);
    }

    /**
     * Update existing city. Don't change id
     *
     * @throws ru.edu.error.CustomException with code CITY_NOT_FOUND if city doesn't exist
     * @throws ru.edu.error.CustomException with code ARGUMENT_ERROR if info is incorrect
     */
    public CityInfo update(CityInfo info) {

        CityInfo foundCity = repository.getCityByName(info.getName());
        if (Objects.isNull(foundCity)) {
            throw new CustomException("City with name=" + info.getName() + " not found in database. Can not update",
                    Errors.CITY_NOT_FOUND);
        }
        if (isIncorrectCityInfo(info)) {
            throw new CustomException("Can not update() ERROR", Errors.ARGUMENT_ERROR);
        }
        return repository.update(info);
    }

    /**
     * Delete city by id
     *
     * @throws ru.edu.error.CustomException with code CITY_NOT_FOUND if city doesn't exist
     */
    public CityInfo delete(String cityId) {

        CityInfo foundCity = repository.getCityById(cityId);
        if (Objects.isNull(foundCity)) {
            throw new CustomException("Can not delete city with id=" + cityId + ". City doesn't exist",
                    Errors.CITY_NOT_FOUND);
        }
        return repository.delete(cityId);
    }

    /**
     * Get city weather
     *
     * @throws ru.edu.error.CustomException with code CITY_NOT_FOUND if city doesn't exist
     */
    public String getTemperature(String cityId) {

        CityInfo foundCity = repository.getCityById(cityId);
        if (Objects.isNull(foundCity)) {
            throw new CustomException("Can not getTemperature() for city with id=" + cityId + ". City doesn't exist",
                    Errors.CITY_NOT_FOUND);
        }

        return String.valueOf(Math.round(weatherCache.getWeatherInfo(foundCity.getName()).getTemperature()));
    }

    public WeatherInfo getWeatherInfo(String cityId) {

        CityInfo foundCity = repository.getCityById(cityId);
        if (Objects.isNull(foundCity)) {
            throw new CustomException("Can not getWeatherInfo() for city with id=" + cityId + ". City doesn't exist",
                    Errors.CITY_NOT_FOUND);
        }

        return weatherCache.getWeatherInfo(foundCity.getName());
    }

    /**
     * Get city distance
     *
     * @throws ru.edu.error.CustomException with code CITY_NOT_FOUND if city doesn't exist
     */
    public int getDistance(String fromCityId, String toCityId) {

        final double RADIUS_OF_EARTH = 6_372_795; // metres
        CityInfo fromCity = repository.getCityById(fromCityId);
        CityInfo toCity = repository.getCityById(toCityId);

        if (Objects.isNull(fromCity)) {
            throw new CustomException("Can not getDistance() from city with id=" + fromCityId + ". City doesn't exist",
                    Errors.CITY_NOT_FOUND);
        }
        if (Objects.isNull(toCity)) {
            throw new CustomException("Can not getDistance() to city with id=" + toCityId + ". City doesn't exist",
                    Errors.CITY_NOT_FOUND);
        }

        double cosSrcLat = Math.cos(fromCity.getLatitude());
        double cosDestLat = Math.cos(toCity.getLatitude());
        double sinSrcLat = Math.sin(fromCity.getLatitude());
        double sinDestLat = Math.sin(toCity.getLatitude());
        double cosDeltaLon = Math.cos(toCity.getLongitude() - fromCity.getLongitude());
        double sinDeltaLon = Math.sin(toCity.getLongitude() - fromCity.getLongitude());

        double angularDiff = Math.atan(
                Math.sqrt(Math.pow(cosDestLat * sinDeltaLon, 2) +
                        Math.pow(cosSrcLat * sinDestLat - sinSrcLat * cosDestLat * cosDeltaLon, 2)
                ) / (sinSrcLat * sinDestLat + cosSrcLat * cosDestLat * cosDeltaLon)
        );

        int distance = (int) Math.round((angularDiff * RADIUS_OF_EARTH / 1000));

        return Math.abs(distance);
    }

    public List<CityInfo> getCitiesNear(String cityId, int radius) {

        if (getCityById(cityId) != null) {
            List<CityInfo> citiesNear = repository.getAll().stream()
                    .filter(city -> getDistance(cityId, city.getId()) <= radius && getDistance(cityId, city.getId()) != 0)
                    .collect(Collectors.toList());
            return citiesNear;
        } else {
            throw new CustomException("Can not get cities near city with the id=" + cityId, Errors.CITY_NOT_FOUND);
        }
    }

    private boolean isIncorrectCityInfo(CityInfo info) {

        boolean check =  weatherCache.getWeatherInfo(info.getName()) == null ||
                info.getPopulation() < 0 ||
                (info.getLatitude() < -Math.PI / 2 || info.getLatitude() > Math.PI / 2) ||
                (info.getLongitude() < -Math.PI || info.getLongitude() > Math.PI);

        return check;
    }

    public String getImageReference(String cityName) {

        return imageAndGeoProvider.getImageReference(cityName);
    }

    public double[] getGeoCode(String cityName) {

        return imageAndGeoProvider.getGeoCode(cityName);
    }
}