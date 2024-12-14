package edu.jewel.hotelbookingapp.initialize;

import edu.jewel.hotelbookingapp.model.*;
import edu.jewel.hotelbookingapp.model.enums.RoleType;
import edu.jewel.hotelbookingapp.model.enums.RoomType;
import edu.jewel.hotelbookingapp.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class TestDataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AdminRepository adminRepository;
    private final CustomerRepository customerRepository;
    private final HotelManagerRepository hotelManagerRepository;
    private final PasswordEncoder passwordEncoder;
    private final AddressRepository addressRepository;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final AvailabilityRepository availabilityRepository;

    @Override
    @Transactional
    public void run(String... args) {
        try {
            log.warn("Checking if test data persistence is required...");

            if (roleRepository.count() == 0 && userRepository.count() == 0) {
                log.info("Initiating test data persistence");

                // Create Roles
                Role adminRole = new Role(RoleType.ADMIN);
                Role customerRole = new Role(RoleType.CUSTOMER);
                Role hotelManagerRole = new Role(RoleType.HOTEL_MANAGER);

                roleRepository.saveAll(Arrays.asList(adminRole, customerRole, hotelManagerRole));
                log.info("Role data persisted");

                // Create Initial Users
                List<User> initialUsers = createInitialUsers(adminRole, customerRole, hotelManagerRole);
                userRepository.saveAll(initialUsers);

                // Create User-Specific Entities
                createUserSpecificEntities(initialUsers);

                // Create Addresses
                List<Address> addresses = createAddresses();
                addressRepository.saveAll(addresses);

                // Create Hotels with Managers
                List<Hotel> hotels = createHotels(addresses);
                hotelRepository.saveAll(hotels);

                // Create Rooms for Hotels
                List<Room> rooms = createRooms(hotels);
                roomRepository.saveAll(rooms);

                // Create Availability
                List<Availability> availabilities = createAvailability(hotels, rooms);
                availabilityRepository.saveAll(availabilities);

                log.info("All test data persisted successfully");
            } else {
                log.info("Test data persistence is not required");
            }
            log.warn("App ready");
        } catch (DataAccessException e) {
            log.error("Exception occurred during data persistence: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected exception occurred: " + e.getMessage());
        }
    }

    private List<User> createInitialUsers(Role adminRole, Role customerRole, Role hotelManagerRole) {
        return Arrays.asList(
                User.builder().username("admin@hotel.com").password(passwordEncoder.encode("1"))
                        .name("Abdul").lastName("Kader").role(adminRole).build(),
                User.builder().username("customer1@hotel.com").password(passwordEncoder.encode("1"))
                        .name("Shafiqul").lastName("Islam").role(customerRole).build(),
                User.builder().username("manager1@hotel.com").password(passwordEncoder.encode("1"))
                        .name("Mostafa").lastName("Rahman").role(hotelManagerRole).build(),
                User.builder().username("manager2@hotel.com").password(passwordEncoder.encode("1"))
                        .name("Mahmud").lastName("Hossain").role(hotelManagerRole).build(),
                // Add more initial users for additional managers
                User.builder().username("manager3@hotel.com").password(passwordEncoder.encode("1"))
                        .name("Kabir").lastName("Hassan").role(hotelManagerRole).build(),
                User.builder().username("manager4@hotel.com").password(passwordEncoder.encode("1"))
                        .name("Amina").lastName("Begum").role(hotelManagerRole).build()
        );
    }

    private void createUserSpecificEntities(List<User> users) {
        List<Admin> admins = new ArrayList<>();
        List<Customer> customers = new ArrayList<>();
        List<HotelManager> hotelManagers = new ArrayList<>();

        for (User user : users) {
            switch (user.getRole().getRoleType()) {
                case ADMIN:
                    admins.add(Admin.builder().user(user).build());
                    break;
                case CUSTOMER:
                    customers.add(Customer.builder().user(user).build());
                    break;
                case HOTEL_MANAGER:
                    hotelManagers.add(HotelManager.builder().user(user).build());
                    break;
            }
        }

        adminRepository.saveAll(admins);
        customerRepository.saveAll(customers);
        hotelManagerRepository.saveAll(hotelManagers);
    }

    private List<Address> createAddresses() {
        return Arrays.asList(
                Address.builder().addressLine("Gulshan-1, Dhaka").city("Dhaka").country("Bangladesh").build(),
                Address.builder().addressLine("Banani, Dhaka").city("Dhaka").country("Bangladesh").build(),
                Address.builder().addressLine("Mirpur-10, Dhaka").city("Dhaka").country("Bangladesh").build(),
                Address.builder().addressLine("Zindabazar, Sylhet").city("Sylhet").country("Bangladesh").build(),
                Address.builder().addressLine("Ambarkhana, Sylhet").city("Sylhet").country("Bangladesh").build(),
                Address.builder().addressLine("Shahporan, Sylhet").city("Sylhet").country("Bangladesh").build(),
                Address.builder().addressLine("Sea Beach Road, Cox's Bazar").city("Cox's Bazar").country("Bangladesh").build(),
                // Add more addresses as needed
                Address.builder().addressLine("Agrabad, Chittagong").city("Chittagong").country("Bangladesh").build()
        );
    }

    private List<Hotel> createHotels(List<Address> addresses) {
        List<HotelManager> managers = hotelManagerRepository.findAll();
        return Arrays.asList(
                Hotel.builder().name("Hotel Sarina Dhaka").address(addresses.get(0)).hotelManager(managers.get(0)).build(),
                Hotel.builder().name("The Westin Dhaka").address(addresses.get(1)).hotelManager(managers.get(1)).build(),
                Hotel.builder().name("Lakeshore Hotel & Apartments").address(addresses.get(2)).hotelManager(managers.get(2)).build(),
                Hotel.builder().name("Jewel's Riverside Retreat").address(addresses.get(3)).hotelManager(managers.get(3)).build(),
                Hotel.builder().name("Rahman Comfort Inn").address(addresses.get(4)).hotelManager(managers.get(3)).build(),
                Hotel.builder().name("Shahporan Family Stay").address(addresses.get(5)).hotelManager(managers.get(2)).build(),
                Hotel.builder().name("Seaside Noor Guest House").address(addresses.get(6)).hotelManager(managers.get(0)).build(),
                Hotel.builder().name("Agrabad Comfort Rooms").address(addresses.get(7)).hotelManager(managers.get(1)).build()
        );
    }

    private List<Room> createRooms(List<Hotel> hotels) {
        List<Room> rooms = new ArrayList<>();
        for (Hotel hotel : hotels) {
            rooms.add(Room.builder()
                    .roomType(RoomType.SINGLE)
                    .pricePerNight(4000)
                    .roomCount(5)
                    .hotel(hotel)
                    .build());
            rooms.add(Room.builder()
                    .roomType(RoomType.DOUBLE)
                    .pricePerNight(7000)
                    .roomCount(3)
                    .hotel(hotel)
                    .build());
        }
        return rooms;
    }

    private List<Availability> createAvailability(List<Hotel> hotels, List<Room> rooms) {
        List<Availability> availabilities = new ArrayList<>();
        for (int i = 0; i < hotels.size(); i++) {
            availabilities.add(Availability.builder()
                    .hotel(hotels.get(i))
                    .room(rooms.get(i * 2))
                    .date(LocalDate.of(2023, 12, 1 + i))
                    .availableRooms(3)
                    .build());
        }
        return availabilities;
    }
}