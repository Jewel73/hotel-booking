package edu.jewel.hotelbookingapp.controller;

import edu.jewel.hotelbookingapp.model.Hotel;
import edu.jewel.hotelbookingapp.service.PythonDataExtractService;
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

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class ChatController {
   private final PythonDataExtractService pythonDataExtractService;

    @PostMapping
    private ResponseEntity<List<Hotel>> getHotels( @RequestBody SearchText searchText){
        var hotels = pythonDataExtractService.extractInfo(searchText.getText());
        return ResponseEntity.status(HttpStatus.OK).body(hotels);

    }
}

@Getter
@Setter
class SearchText{
    private String text;
}
