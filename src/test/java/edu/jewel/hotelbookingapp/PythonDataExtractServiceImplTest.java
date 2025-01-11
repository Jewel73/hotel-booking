package edu.jewel.hotelbookingapp;

import edu.jewel.hotelbookingapp.model.Hotel;
import edu.jewel.hotelbookingapp.repository.HotelRepository;
import edu.jewel.hotelbookingapp.service.impl.PythonDataExtractServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class PythonDataExtractServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private PythonDataExtractServiceImpl pythonDataExtractService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExtractInfo() {
        // Mock API response
        Map<String, Object> mockResponse = Map.of(
                "location", "Dhaka",
                "price", Map.of(
                        "condition", ">=",
                        "value", 500.0
                )
        );
        ResponseEntity<Map> responseEntity = ResponseEntity.ok(mockResponse);
        when(restTemplate.postForEntity(any(String.class), any(Map.class), eq(Map.class)))
                .thenReturn(responseEntity);

        // Call the method
        String text = "Find hotels in Dhaka with price above 500";
        Map<String, Object> result = new HashMap<>(); //pythonDataExtractService.extractInfo(text);
        List<Hotel> hotels = pythonDataExtractService.findHotelsByCriteria(result);

        // Verify results
        assertEquals("Dhaka", result.get("location"));
        assertEquals(">=", ((Map) result.get("price")).get("condition"));
        assertEquals(500.0, ((Map) result.get("price")).get("value"));
    }

}
