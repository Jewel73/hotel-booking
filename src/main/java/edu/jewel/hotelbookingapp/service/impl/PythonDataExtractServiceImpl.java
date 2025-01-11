package edu.jewel.hotelbookingapp.service.impl;

import edu.jewel.hotelbookingapp.model.Hotel;
import edu.jewel.hotelbookingapp.repository.HotelRepository;
import edu.jewel.hotelbookingapp.service.PythonDataExtractService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class PythonDataExtractServiceImpl implements PythonDataExtractService {
    private final RestTemplate restTemplate;
    private final HotelRepository hotelRepository;

    public PythonDataExtractServiceImpl(RestTemplate restTemplate, HotelRepository hotelRepository) {
        this.restTemplate = restTemplate;
        this.hotelRepository = hotelRepository;
    }

    @Override
    public List<Hotel> extractInfo(String text) {
        String url = "http://localhost:5000/extract";
        Map<String, String> request = Map.of("text", text);
        // Call Python model API
        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
        return findHotelsByCriteria(response.getBody());
    }

    public List<Hotel> findHotelsByCriteria(Map<String, Object> criteria) {
        // Extract location and price criteria
        String location = (String) criteria.get("location");
        Map<String, Object> priceCriteria = (Map<String, Object>) criteria.get("price");
        String priceCondition = (String) priceCriteria.get("condition");
        Double priceValue = (Double) priceCriteria.get("value");

        // Call the repository to find hotels based on extracted criteria
        switch (priceCondition) {
            case ">=":
                return hotelRepository.findByCityAndPriceGreaterThanEqual(location, priceValue);
            case "<=":
                return hotelRepository.findByCityAndPriceLessThanEqual(location, priceValue);
            case "=":
                return hotelRepository.findByCityAndPriceEqual(location, priceValue);
            default:
                throw new IllegalArgumentException("Unsupported price condition: " + priceCondition);
        }
    }
}
