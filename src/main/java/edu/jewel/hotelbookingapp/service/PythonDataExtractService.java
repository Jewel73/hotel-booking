package edu.jewel.hotelbookingapp.service;

import edu.jewel.hotelbookingapp.model.Hotel;

import java.util.List;
import java.util.Map;

public interface PythonDataExtractService {
    public List<Hotel> extractInfo(String text);
}
