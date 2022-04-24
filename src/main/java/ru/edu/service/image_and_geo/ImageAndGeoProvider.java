package ru.edu.service.image_and_geo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.edu.error.CustomException;

@Component
public class ImageAndGeoProvider {

    private RestTemplate restTemplate;
    private String appKey;
    private ObjectMapper mapper = new ObjectMapper();

    @Value("${placesApiKey}")
    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getImageReference(String cityName) {

        JsonNode node = null;
        String info;
        String uri = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input=" + cityName + "&key=" + appKey + "&inputtype=textquery&fields=name,photos";

        try {
            info = restTemplate.getForObject(uri, String.class);
        } catch (Exception ex) {
            throw new CustomException("Can not get imageRef for city with name=" + cityName, ex.getMessage());
        }

        try {
            node = mapper.readTree(info);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        String imageReference = node.get("candidates").get(0).get("photos").get(0).get("photo_reference").asText();

        String imageSource = "https://maps.googleapis.com/maps/api/place/photo?photoreference=" + imageReference + "&key=" + appKey + "&maxwidth=500&maxheight=500";

        return imageSource;
    }

    public double[] getGeoCode(String cityName) {

        JsonNode node = null;
        String info;
        String uri = "https://maps.googleapis.com/maps/api/geocode/json?address=" + cityName + "&key=" + appKey;

        try {
            info = restTemplate.getForObject(uri, String.class);
        } catch (Exception ex) {
            throw new CustomException("Can not get geoCode for city with name=" + cityName, ex.getMessage());
        }

        try {
            node = mapper.readTree(info);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        double latitude = node.get("results").get(0).get("geometry").get("location").get("lat").asDouble();
        double longitude = node.get("results").get(0).get("geometry").get("location").get("lng").asDouble();

        double[] geoCode = {latitude, longitude};

        return geoCode;
    }
}