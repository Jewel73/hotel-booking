package edu.jewel.hotelbookingapp.controller;

import edu.jewel.hotelbookingapp.service.PythonDataExtractService;
import edu.jewel.hotelbookingapp.service.RoomService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class ChatController {
   private final PythonDataExtractService pythonDataExtractService;

   private final RoomService roomService;

    @PostMapping
    private ResponseEntity<List<ChatHotelDto>> getHotels( @RequestBody SearchText searchText){
        var hotels = pythonDataExtractService.extractInfo(searchText.getText());
        var chatHotels = hotels.stream()
                .map(hotel ->
                        new ChatHotelDto(hotel.getId(), hotel.getName(), hotel.getAddress().toString())
                )
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(chatHotels);

    }
}

@Getter
@Setter
class SearchText{
    private String text;
}


record ChatHotelDto(Long hotelId, String hotelName, String addres){}