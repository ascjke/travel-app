package ru.edu.service.weather;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Weather cache.
 */
@Component
public class WeatherCache {

    private final Map<String, WeatherInfo> cache = new HashMap<>();
    private WeatherProvider weatherProvider;

    @Autowired
    public void setWeatherProvider(WeatherProvider weatherProvider) {
        this.weatherProvider = weatherProvider;
    }

    /**
     * Default constructor.
     */
    public WeatherCache() {
    }

    /**
     * Get ACTUAL weather info for current city or null if current city not found.
     * If cache doesn't contain weather info OR contains NOT ACTUAL info then we should download info
     * If you download weather info then you should set expiry time now() plus 5 minutes.
     * If you can't download weather info then remove weather info for current city from cache.
     *
     * @param city - city
     * @return actual weather info
     */
    public WeatherInfo getWeatherInfo(String city) {

        city = city.toLowerCase();

        synchronized (cache) {
            if (!cache.containsKey(city)) {
                cache.put(city, weatherProvider.get(city));
            } else {
                if (LocalDateTime.now().isAfter(cache.get(city).getExpiryTime())) {
                    removeWeatherInfo(city);
                    cache.put(city, weatherProvider.get(city));
                }
            }
            return cache.get(city);
        }
    }

    /**
     * Remove weather info from cache.
     **/
    public void removeWeatherInfo(String city) {

        city = city.toLowerCase();
        cache.remove(city);
    }
}
