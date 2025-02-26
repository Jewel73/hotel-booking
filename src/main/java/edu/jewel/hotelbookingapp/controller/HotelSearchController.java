package edu.jewel.hotelbookingapp.controller;

import edu.jewel.hotelbookingapp.model.Customer;
import edu.jewel.hotelbookingapp.model.Review;
import edu.jewel.hotelbookingapp.model.dto.HotelAvailabilityDTO;
import edu.jewel.hotelbookingapp.model.dto.HotelSearchDTO;
import edu.jewel.hotelbookingapp.model.dto.UserDTO;
import edu.jewel.hotelbookingapp.service.*;
import edu.jewel.hotelbookingapp.service.impl.PhotoStorageService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HotelSearchController {

    private final HotelSearchService hotelSearchService;
    private final ReviewService reviewService;
    private final CustomerService customerService;
    private final BookingService bookingService;
    private final UserService userService;
    private final PhotoStorageService photoStorageService;

    @GetMapping("/search")
    public String showSearchForm(@ModelAttribute("hotelSearchDTO") HotelSearchDTO hotelSearchDTO) {
        return "hotelsearch/search";
    }


    @PostMapping("/search")
    public String findAvailableHotelsByCityAndDate(@Valid @ModelAttribute("hotelSearchDTO") HotelSearchDTO hotelSearchDTO, BindingResult result) {
        if (result.hasErrors()) {
            return "hotelsearch/search";
        }
        try {
            validateCheckinAndCheckoutDates(hotelSearchDTO.getCheckinDate(), hotelSearchDTO.getCheckoutDate());
        } catch (IllegalArgumentException e) {
            result.rejectValue("checkoutDate", null, e.getMessage());
            return "hotelsearch/search";
        }

        // Redirect to a new GET endpoint with parameters for data fetching. Allows page refreshing
        return String.format("redirect:/search-results?city=%s&checkinDate=%s&checkoutDate=%s", hotelSearchDTO.getCity(), hotelSearchDTO.getCheckinDate(), hotelSearchDTO.getCheckoutDate());
    }

    @GetMapping("/search-results")
    public String showSearchResults(@RequestParam String city, @RequestParam String checkinDate, @RequestParam String checkoutDate, Model model, RedirectAttributes redirectAttributes) {
        try {
            LocalDate parsedCheckinDate = LocalDate.parse(checkinDate);
            LocalDate parsedCheckoutDate = LocalDate.parse(checkoutDate);

            validateCheckinAndCheckoutDates(parsedCheckinDate, parsedCheckoutDate);

            log.info("Searching hotels for city {} between dates {} and {}", city, checkinDate, checkoutDate);
            List<HotelAvailabilityDTO> hotels = hotelSearchService.findAvailableHotelsByCityAndDate(city, parsedCheckinDate, parsedCheckoutDate);

            if (hotels.isEmpty()) {
                model.addAttribute("noHotelsFound", true);
            }

            long durationDays = ChronoUnit.DAYS.between(parsedCheckinDate, parsedCheckoutDate);

            model.addAttribute("hotels", hotels);
            model.addAttribute("city", city);
            model.addAttribute("days", durationDays);
            model.addAttribute("checkinDate", checkinDate);
            model.addAttribute("checkoutDate", checkoutDate);

        } catch (DateTimeParseException e) {
            log.error("Invalid date format provided for URL search", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid date format. Please use the search form.");
            return "redirect:/search";
        } catch (IllegalArgumentException e) {
            log.error("Invalid arguments provided for URL search", e);
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/search";
        } catch (Exception e) {
            log.error("An error occurred while searching for hotels", e);
            redirectAttributes.addFlashAttribute("errorMessage", "An unexpected error occurred. Please try again later.");
            return "redirect:/search";
        }

        return "hotelsearch/search-results";
    }

    @GetMapping("uploads/hotels/{filename}")
    public ResponseEntity<Resource> fetchPhoto(@PathVariable String filename) {
        try {
            // Get the photo resource from the service
            Resource photo = photoStorageService.getPhoto(filename);

            // Determine the file's content type
            String contentType = Files.probeContentType(Paths.get(photo.getFile().getAbsolutePath()));

            // Set a default content type if it couldn't be determined
            if (contentType == null) {
                contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                    .body(photo);
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/explore")
    public String recomendationPage(Model model, RedirectAttributes redirectAttributes) {
        if (true)
            return "hotelsearch/recommendation";

        try {

            List<HotelAvailabilityDTO> hotels = hotelSearchService.findAll();

            if (hotels.isEmpty()) {
                model.addAttribute("noHotelsFound", true);
            }

            long durationDays = ChronoUnit.DAYS.between(LocalDate.now(), LocalDate.now());

            model.addAttribute("hotels", hotels);
            model.addAttribute("days", durationDays);

        } catch (DateTimeParseException e) {
            log.error("Invalid date format provided for URL search", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid date format. Please use the search form.");
            return "redirect:/search";
        } catch (IllegalArgumentException e) {
            log.error("Invalid arguments provided for URL search", e);
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/search";
        } catch (Exception e) {
            log.error("An error occurred while searching for hotels", e);
            redirectAttributes.addFlashAttribute("errorMessage", "An unexpected error occurred. Please try again later.");
            return "redirect:/search";
        }

        return "hotelsearch/recommendation";
    }



    @GetMapping("/hotel-details/{id}")
    public String showHotelDetails(@PathVariable Long id, @RequestParam String checkinDate, @RequestParam String checkoutDate, Model model, RedirectAttributes redirectAttributes) {
        try {
            LocalDate parsedCheckinDate = LocalDate.parse(checkinDate);
            LocalDate parsedCheckoutDate = LocalDate.parse(checkoutDate);

            //validateCheckinAndCheckoutDates(parsedCheckinDate, parsedCheckoutDate);

            HotelAvailabilityDTO hotelAvailabilityDTO = hotelSearchService.findAvailableHotelById(id, parsedCheckinDate, parsedCheckoutDate);

            long durationDays = ChronoUnit.DAYS.between(parsedCheckinDate, parsedCheckoutDate);
            List<Review> reviews = reviewService.getReviewsByHotel(id);
            long userId = getLoggedInUserId();
            Customer customer = customerService.findByUserId(userId)
                    .orElseThrow(() -> new EntityNotFoundException("Customer not found with user ID: " + userId));
            boolean canReviewGive = bookingService.findByCustomerAndHotelId(customer.getId(), id).size() >0 ? true: false;

            model.addAttribute("hotel", hotelAvailabilityDTO);
            model.addAttribute("durationDays", durationDays);
            model.addAttribute("checkinDate", checkinDate);
            model.addAttribute("checkoutDate", checkoutDate);
            model.addAttribute("reviews", reviews);
            model.addAttribute("canReview", canReviewGive);
            model.addAttribute("customerId", customer.getId());

            return "hotelsearch/hotel-details";


        } catch (DateTimeParseException e) {
            log.error("Invalid date format provided", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid date format. Please use the search form.");
            return "redirect:/search";
        } catch (IllegalArgumentException e) {
            log.error("Invalid arguments provided for URL search", e);
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/search";
        } catch (EntityNotFoundException e) {
            log.error("No hotel found with ID {}", id);
            redirectAttributes.addFlashAttribute("errorMessage", "The selected hotel is no longer available. Please start a new search.");
            return "redirect:/search";
        } catch (Exception e) {
            log.error("An error occurred while searching for hotels", e);
            redirectAttributes.addFlashAttribute("errorMessage", "An unexpected error occurred. Please try again later.");
            return "redirect:/search";
        }
    }

    private void validateCheckinAndCheckoutDates(LocalDate checkinDate, LocalDate checkoutDate) {
        if (checkinDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Check-in date cannot be in the past");
        }
        if (checkoutDate.isBefore(checkinDate.plusDays(1))) {
            throw new IllegalArgumentException("Check-out date must be after check-in date");
        }
    }

    private void parseAndValidateBookingDates(String checkinDate, String checkoutDate) {
        LocalDate parsedCheckinDate = LocalDate.parse(checkinDate);
        LocalDate parsedCheckoutDate = LocalDate.parse(checkoutDate);
        validateCheckinAndCheckoutDates(parsedCheckinDate, parsedCheckoutDate);
    }

    private Long getLoggedInUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        UserDTO userDTO = userService.findUserDTOByUsername(username);
        log.info("Fetched logged in user ID: {}", userDTO.getId());
        return userDTO.getId();
    }

}