package ru.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.edu.service.CityInfo;
import ru.edu.service.CityService;
import ru.edu.service.weather.WeatherInfo;

import java.util.*;

@Controller
@RequestMapping(value = "/travel")
public class CityController {

    private CityService cityService;
    private int radius;

    @Autowired
    public void setCityService(CityService cityService) {
        this.cityService = cityService;
    }

    @Value("${radius}")
    public void setRadius(int radius) {
        this.radius = radius;
    }

    /**
     * Get all cities view.
     */
    // GET /travel
    @GetMapping
    public ModelAndView getAllCitiesView() {

        ModelAndView modelAndView = new ModelAndView();
        Map<CityInfo, String> citiesWithTemp = new TreeMap<>();
        List<CityInfo> cities = cityService.getAll();

        for (int i = 0; i < cities.size(); i++) {
            citiesWithTemp.put(cities.get(i), cityService.getTemperature(cities.get(i).getId()));
        }

        modelAndView.setViewName("all");
        modelAndView.addObject("cityList", citiesWithTemp);
        modelAndView.addObject("cityService", cityService);
        return modelAndView;
    }

    /**
     * Get city view.
     *
     * @param cityId - city id
     */
    // GET /travel/city?cityId=
    @GetMapping("/city")
    public ModelAndView getCityView(@RequestParam("cityId") String cityId) {

        ModelAndView modelAndView = new ModelAndView();
        CityInfo city = cityService.getCityById(cityId);

        List<CityInfo> nearCities = cityService.getCitiesNear(cityId, radius);
        Map<CityInfo, Integer> distances = new TreeMap<>();

        for (CityInfo nearCity : nearCities) {
            distances.put(nearCity, cityService.getDistance(cityId, nearCity.getId()));
        }

        String population_f = String.format(Locale.FRANCE, "%,d", city.getPopulation());

        String latitude;
        String longitude;
        if (city.getLatitude() < 0) {
            String tmp = doubleToDegree(city.getLatitude()).substring(1);
            latitude = tmp + " ю.ш.";
        } else {
            latitude = doubleToDegree(city.getLatitude()) + " с.ш.";
        }

        if (city.getLongitude() < 0) {
            String tmp = doubleToDegree(city.getLongitude()).substring(1);
            longitude = tmp + " з.д.";
        } else {
            longitude = doubleToDegree(city.getLongitude()) + " в.д.";
        }

        String imgSrc = cityService.getImageReference(city.getName());

        modelAndView.setViewName("city");
        modelAndView.addObject("city", city);
        modelAndView.addObject("population", population_f);
        modelAndView.addObject("latitude", latitude);
        modelAndView.addObject("longitude", longitude);
        modelAndView.addObject("distances", distances);
        modelAndView.addObject("imgSrc", imgSrc);
        modelAndView.addObject("radius", radius);

        return modelAndView;
    }

    /**
     * Get create new city view.
     */
    // GET /travel/create
    @GetMapping("/create")
    public ModelAndView getCreateCityView() {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("create");
        return modelAndView;
    }

    /**
     * Create new city.
     */
    @PostMapping("/create")
    public ModelAndView createCity(CityInfo info,
                                   @RequestParam("population_str") String populationStr) {

        ModelAndView modelAndView = new ModelAndView();

        WeatherInfo weatherInfo = cityService.getWeatherCache().getWeatherInfo(info.getName());
        List<String> cityNames = new ArrayList<>();
        for (CityInfo city : cityService.getAll()) {
            cityNames.add(city.getName().toLowerCase());
        }

        if (weatherInfo == null || cityNames.contains(info.getName().toLowerCase())) {
            modelAndView.addObject("cityName", info.getName());
            modelAndView.setViewName("error_1");
            return modelAndView;
        } else if (populationStr.equals("")) {
            modelAndView.setViewName("error_3");
            return modelAndView;
        } else if (!populationStr.matches("\\d+") || Integer.parseInt(populationStr) < 0) {
            modelAndView.addObject("population", populationStr);
            modelAndView.setViewName("error_2");
            return modelAndView;
        }

        double latitude = cityService.getGeoCode(info.getName())[0];
        double longitude = cityService.getGeoCode(info.getName())[1];

        info.setLatitude(Math.toRadians(latitude));
        info.setLongitude(Math.toRadians(longitude));
        info.setPopulation(Integer.parseInt(populationStr));

        CityInfo newCity = cityService.create(info);

        return getCityView(newCity.getId());
    }

    @GetMapping("/edit")
    public ModelAndView getEditCityView(@RequestParam("cityId") String id) {

        ModelAndView modelAndView = new ModelAndView();
        CityInfo city = cityService.getCityById(id);

        String latitude = doubleToDegree(city.getLatitude());
        String latDeg = latitude.substring(0, latitude.indexOf('°'));
        String latMin = latitude.substring(latitude.indexOf('°') + 2, latitude.indexOf('′'));
        String latSec = latitude.substring(latitude.indexOf('′') + 2, latitude.indexOf('″'));

        String longitude = doubleToDegree(city.getLongitude());
        String lonDeg = longitude.substring(0, longitude.indexOf('°'));
        String lonMin = longitude.substring(longitude.indexOf('°') + 2, longitude.indexOf('′'));
        String lonSec = longitude.substring(longitude.indexOf('′') + 2, longitude.indexOf('″'));

        modelAndView.setViewName("edit");
        modelAndView.addObject("city", city);
        modelAndView.addObject("latDeg", Integer.valueOf(latDeg));
        modelAndView.addObject("latMin", Integer.valueOf(latMin));
        modelAndView.addObject("latSec", Integer.valueOf(latSec));
        modelAndView.addObject("lonDeg", Integer.valueOf(lonDeg));
        modelAndView.addObject("lonMin", Integer.valueOf(lonMin));
        modelAndView.addObject("lonSec", Integer.valueOf(lonSec));

        return modelAndView;
    }

    @PostMapping("/edit")
    public ModelAndView editCity(@RequestParam("cityId") String id,
                                 @RequestParam("name") String name,
                                 @RequestParam("description") String description,
                                 @RequestParam("climate") String climate,
                                 @RequestParam("population") int population,
                                 @RequestParam("latDeg") int latDeg,
                                 @RequestParam("latMin") int latMin,
                                 @RequestParam("latSec") int latSec,
                                 @RequestParam("lonDeg") int lonDeg,
                                 @RequestParam("lonMin") int lonMin,
                                 @RequestParam("lonSec") int lonSec) {

        double latitudeDegree = latDeg + (latMin * 60 + latSec) / 3600.0;
        double longitudeDegree = lonDeg + (lonMin * 60 + lonSec) / 3600.0;

        CityInfo city = new CityInfo();
        city.setId(id);
        city.setName(name);
        city.setDescription(description);
        city.setClimate(climate);
        city.setPopulation(population);
        city.setLatitude(Math.toRadians(latitudeDegree));
        city.setLongitude(Math.toRadians(longitudeDegree));

        cityService.update(city);
        return getCityView(id);
    }

    @PostMapping("/delete")
    public ModelAndView delete(String cityId) {

        cityService.delete(cityId);
        return getAllCitiesView();
    }

    public static String doubleToDegree(double radians) {

        double value = Math.toDegrees(radians);
        int degree = (int) value;
        double rawMinute = Math.abs((value % 1) * 60);
        int minute = (int) rawMinute;
        int second = (int) Math.round((rawMinute % 1) * 60);

        return String.format("%d° %d′ %d″", degree, minute, second);
    }
}