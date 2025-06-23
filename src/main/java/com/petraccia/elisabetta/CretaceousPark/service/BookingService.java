package com.petraccia.elisabetta.CretaceousPark.service;

import com.petraccia.elisabetta.CretaceousPark.dto.BookingDTO;
import com.petraccia.elisabetta.CretaceousPark.exception.BadRequestException;
import com.petraccia.elisabetta.CretaceousPark.exception.ResourceNotFoundException;
import com.petraccia.elisabetta.CretaceousPark.mapper.BookingMapper;
import com.petraccia.elisabetta.CretaceousPark.model.Bookable;
import com.petraccia.elisabetta.CretaceousPark.model.Booking;
import com.petraccia.elisabetta.CretaceousPark.model.Planner;
import com.petraccia.elisabetta.CretaceousPark.model.Restaurant;
import com.petraccia.elisabetta.CretaceousPark.repository.BookableRepository;
import com.petraccia.elisabetta.CretaceousPark.repository.BookingRepository;
import com.petraccia.elisabetta.CretaceousPark.repository.PlannerRepository;
import com.petraccia.elisabetta.CretaceousPark.repository.RestaurantRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final PlannerRepository plannerRepository;
    private final RestaurantRepository restaurantRepository;
    private final BookableRepository bookableRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository, PlannerRepository plannerRepository, RestaurantRepository restaurantRepository, BookableRepository bookableRepository) {
        this.bookingRepository = bookingRepository;
        this.plannerRepository = plannerRepository;
        this.restaurantRepository = restaurantRepository;
        this.bookableRepository = bookableRepository;
    }

    public List<BookingDTO> getBookingsByCustomerAuthUserId(Long authUserId) {
        List<Booking> bookings = bookingRepository.findByCustomerAuthUserId(authUserId);
        return bookings.stream()
                .map(BookingMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Long getPlannerIdByAuthUserId(Long authUserId) {
        Optional<Planner> plannerOpt = plannerRepository.findByCustomerAuthUserId(authUserId);
        return plannerOpt.map(Planner::getId).orElse(null);
    }

    public boolean isBookingOwnedByAuthUser(Long bookingId, Long authUserId) {
        // recupera booking dal repository, con join per risalire all’authUser id
        Optional<Booking> bookingOpt = bookingRepository.findById(bookingId);
        if (bookingOpt.isEmpty()) {
            return false;
        }

        Booking booking = bookingOpt.get();
        Long bookingAuthUserId = booking.getPlanner().getCustomer().getAuthUser().getId();

        return bookingAuthUserId.equals(authUserId);
    }

    public List<BookingDTO> getAllBookings() {
        return bookingRepository.findAll()
                .stream()
                .map(BookingMapper::toDTO)
                .toList();
    }

    public BookingDTO getBookingById(Long id) {
        if (id == null) {
            throw new BadRequestException("ID must not be null.");
        }

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking  not found with id: " + id));

        return BookingMapper.toDTO(booking);
    }

    public List<BookingDTO> getBookingsByPlannerId(Long plannerId) {
        if (plannerId == null) {
            throw new BadRequestException("Planner ID must not be null.");
        }

        Planner planner = plannerRepository.findById(plannerId)
                .orElseThrow(() -> new ResourceNotFoundException("Planner not found with id: " + plannerId));

        List<Booking> bookings = bookingRepository.findByPlannerId(plannerId);

        if (bookings == null || bookings.isEmpty()) {
            throw new ResourceNotFoundException("No bookings found for Planner id: " + plannerId);
        }

        return bookings.stream()
                .map(BookingMapper::toDTO)
                .toList();
    }

    public BookingDTO saveBooking(BookingDTO bookingDTO) {
        if (bookingDTO.getPlannerId() == null) {
            throw new BadRequestException("Planner ID must not be null.");
        }

        if (bookingDTO.getRestaurantId() != null && bookingDTO.getBookableId() != null) {
            throw new BadRequestException("Booking must be for either a Restaurant or a Bookable, not both.");
        }

        Planner planner = plannerRepository.findById(bookingDTO.getPlannerId())
                .orElseThrow(() -> new ResourceNotFoundException("Planner not found with id: " + bookingDTO.getPlannerId()));

        Restaurant restaurant = null;
        Bookable bookable = null;

        if (bookingDTO.getRestaurantId() != null) {
            restaurant = restaurantRepository.findById(bookingDTO.getRestaurantId())
                    .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + bookingDTO.getRestaurantId()));
        }

        if (bookingDTO.getBookableId() != null) {
            bookable = bookableRepository.findById(bookingDTO.getBookableId())
                    .orElseThrow(() -> new ResourceNotFoundException("Bookable not found with id: " + bookingDTO.getBookableId()));
        }

        Booking bookingToSave = BookingMapper.toEntity(bookingDTO, restaurant, bookable, planner);
        Booking savedBooking = bookingRepository.save(bookingToSave);

        return BookingMapper.toDTO(savedBooking);
    }

    @Transactional
    public void deleteBookingById(Long id) {
        if (id == null) {
            throw new BadRequestException("ID must not be null.");
        }

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));

        bookingRepository.deleteById(id);
    }

    public BookingDTO updateBooking(Long id, BookingDTO bookingDTO) {
        if (id == null) {
            throw new BadRequestException("Booking ID must not be null.");
        }
        if (bookingDTO == null) {
            throw new BadRequestException("Booking data must not be null.");
        }

        Booking existingBooking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));

        // Controllo: non possono essere presenti entrambi
        if (bookingDTO.getRestaurantId() != null && bookingDTO.getBookableId() != null) {
            throw new BadRequestException("Booking must be for either a Restaurant or a Bookable, not both.");
        }

        // Recupero il planner
        Planner planner = plannerRepository.findById(bookingDTO.getPlannerId())
                .orElseThrow(() -> new ResourceNotFoundException("Planner not found with id: " + bookingDTO.getPlannerId()));

        // Recupero opzionale Restaurant o Bookable
        Restaurant restaurant = null;
        Bookable bookable = null;

        if (bookingDTO.getRestaurantId() != null) {
            restaurant = restaurantRepository.findById(bookingDTO.getRestaurantId())
                    .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + bookingDTO.getRestaurantId()));
        }

        if (bookingDTO.getBookableId() != null) {
            bookable = bookableRepository.findById(bookingDTO.getBookableId())
                    .orElseThrow(() -> new ResourceNotFoundException("Bookable not found with id: " + bookingDTO.getBookableId()));
        }

        // Aggiornamento campi
        existingBooking.setDate(bookingDTO.getDate());
        existingBooking.setReservationQty(bookingDTO.getReservationQty());
        existingBooking.setPlanner(planner);
        existingBooking.setRestaurant(restaurant);
        existingBooking.setBookable(bookable);

        Booking updatedBooking = bookingRepository.save(existingBooking);
        return BookingMapper.toDTO(updatedBooking);
    }

}
