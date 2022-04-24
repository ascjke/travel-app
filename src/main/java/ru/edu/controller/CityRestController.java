package ru.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.edu.error.CustomException;
import ru.edu.error.Errors;
import ru.edu.service.CityInfo;
import ru.edu.service.CityService;
import ru.edu.service.weather.WeatherInfo;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(value = "/api/travel")
public class CityRestController {

    private CityService cityService;

    @Autowired
    public void setCityService(CityService cityService) {
        this.cityService = cityService;
    }

    /**
     * Get all.
     *
     */
    // GET /api/travel/all
    @GetMapping("/all")
    public List<CityInfo> getAll() {
        return cityService.getAll();
    }

    /**
     * Get city by id. Returns null if not found.
     *
     * @param cityId - item id
     */
    // GET /api/travel/?cityId=
    @GetMapping("/")
    public CityInfo getCity(@RequestParam("cityId") String cityId) {
        if(cityService.getCityById(cityId) == null){
            return null;
        }
        return cityService.getCityById(cityId);
    }

    /**
     * Create new city.
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public CityInfo create(@RequestBody CityInfo info) {
        return cityService.create(info);
    }

    /**
     * Update existing city. Don't change id
     */
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public CityInfo update(@RequestBody CityInfo info) {
        return cityService.update(info);
    }

    /**
     * Delete city by id.
     *
     */
    // DELETE api/travel/?cityIdToDelete=
    @DeleteMapping("/")
    public CityInfo delete(@RequestParam("cityIdToDelete") String cityId) {
        if (Objects.isNull(cityService.getCityById(cityId))) {
            throw new CustomException("Can not delete city with id=" + cityId + ". City doesn't exist",
                    Errors.CITY_NOT_FOUND);
        }
        return cityService.delete(cityId);
    }

    // GET api/travel?cityIdWeather=
    @GetMapping("/weather")
    public WeatherInfo getWeatherInfo(@RequestParam("cityIdWeather") String cityId) {
        if (Objects.isNull(cityService.getCityById(cityId))) {
            throw new CustomException("Can not getWeather() for city with id=" + cityId + ". City doesn't exist",
                    Errors.CITY_NOT_FOUND);
        }
        return cityService.getWeatherInfo(cityId);
    }
}
