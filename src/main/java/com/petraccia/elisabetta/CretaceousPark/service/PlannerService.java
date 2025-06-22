package com.petraccia.elisabetta.CretaceousPark.service;

import com.petraccia.elisabetta.CretaceousPark.dto.HolidayDTO;
import com.petraccia.elisabetta.CretaceousPark.dto.PlannerDTO;
import com.petraccia.elisabetta.CretaceousPark.exception.BadRequestException;
import com.petraccia.elisabetta.CretaceousPark.exception.ResourceNotFoundException;
import com.petraccia.elisabetta.CretaceousPark.mapper.PlannerMapper;
import com.petraccia.elisabetta.CretaceousPark.model.*;
import com.petraccia.elisabetta.CretaceousPark.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class PlannerService {

    private final PlannerRepository plannerRepository;
    private final CustomerRepository customerRepository;
    private final DayTypeRepository dayTypeRepository;
    private final BookingRepository bookingRepository;
    private final TicketRepository ticketRepository;

    @Autowired
    public PlannerService(PlannerRepository plannerRepository, CustomerRepository customerRepository, DayTypeRepository dayTypeRepository, BookingRepository bookingRepository, TicketRepository ticketRepository) {
        this.plannerRepository = plannerRepository;
        this.customerRepository = customerRepository;
        this.dayTypeRepository = dayTypeRepository;
        this.bookingRepository = bookingRepository;
        this.ticketRepository = ticketRepository;
    }

    public List<PlannerDTO> getAllPlanners() {
        return plannerRepository.findAll()
                .stream()
                .map(PlannerMapper::toDTO)
                .toList();
    }

    public PlannerDTO getPlannerById(Long id) {
        if (id == null) {
            throw new BadRequestException("ID must not be null.");
        }

        Planner planner = plannerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Planner not found with id: " + id));

        return PlannerMapper.toDTO(planner);
    }

    public PlannerDTO getPlannerByCustomerId(Long customerId) {
        if (customerId == null) {
            throw new BadRequestException("Customer ID must not be null.");
        }

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));

        Planner planner = plannerRepository.findByCustomer(customer)
                .orElseThrow(() -> new ResourceNotFoundException("Planner not found for Customer id: " + customerId));

        return PlannerMapper.toDTO(planner);
    }

    public boolean isPublicHoliday(LocalDate date) {
        int year = date.getYear();
        String countryCode = "CR";
        String url = "https://date.nager.at/api/v3/PublicHolidays/" + year + "/" + countryCode;

        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<HolidayDTO[]> response = restTemplate.getForEntity(url, HolidayDTO[].class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                for (HolidayDTO holiday : response.getBody()) {
                    if (date.equals(holiday.getDate())) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error while checking holidays: " + e.getMessage());
        }

        return false;
    }

    public DayType determineDayType(LocalDate date) {
        if (isPublicHoliday(date)) {
            return dayTypeRepository.findByNameIgnoreCase("holiday")
                    .orElseThrow(() -> new ResourceNotFoundException("DayType 'holiday' not found"));
        }

        int day = date.getDayOfMonth();
        int month = date.getMonthValue();
        int dayOfWeek = date.getDayOfWeek().getValue(); // 1 = Monday, 7 = Sunday

        // Weekend: Sabato (6) o Domenica (7)
        if (dayOfWeek == 6 || dayOfWeek == 7) {
            return dayTypeRepository.findByNameIgnoreCase("weekend")
                    .orElseThrow(() -> new ResourceNotFoundException("DayType 'weekend' not found"));
        }

        // Estate: Giugno (6), Luglio (7), Agosto (8)
        if (month >= 6 && month <= 8) {
            return dayTypeRepository.findByNameIgnoreCase("summer")
                    .orElseThrow(() -> new ResourceNotFoundException("DayType 'summer' not found"));
        }

        // Altrimenti: inverno
        return dayTypeRepository.findByNameIgnoreCase("winter")
                .orElseThrow(() -> new ResourceNotFoundException("DayType 'winter' not found"));
    }

    public PlannerDTO savePlanner(PlannerDTO plannerDTO) {
        if (plannerDTO.getCustomerId() == null) {
            throw new BadRequestException("Customer ID must not be null.");
        }

        if (plannerDTO.getDate() == null) {
            throw new BadRequestException("Date must not be null.");
        }

        Customer customer = customerRepository.findById(plannerDTO.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + plannerDTO.getCustomerId()));

        DayType dayType = determineDayType(plannerDTO.getDate());

        Planner plannerToSave = PlannerMapper.toEntity(plannerDTO, dayType, customer);
        Planner savedPlanner = plannerRepository.save(plannerToSave);
        return PlannerMapper.toDTO(savedPlanner);
    }

    public PlannerDTO updatePlanner(Long id, PlannerDTO plannerDTO) {
        if (id == null) {
            throw new BadRequestException("Planner ID must not be null.");
        }

        if (plannerDTO == null) {
            throw new BadRequestException("Planner data must not be null.");
        }

        if (plannerDTO.getCustomerId() == null) {
            throw new BadRequestException("Customer ID must not be null.");
        }

        if (plannerDTO.getDate() == null) {
            throw new BadRequestException("Date must not be null.");
        }

        Planner existingPlanner = plannerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Planner not found with id: " + id));

        Customer customer = customerRepository.findById(plannerDTO.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + plannerDTO.getCustomerId()));

        DayType dayType = determineDayType(plannerDTO.getDate());

        existingPlanner.setDate(plannerDTO.getDate());
        existingPlanner.setDayType(dayType);
        existingPlanner.setCustomer(customer);

        // Gestione tickets
        List<Ticket> tickets = new ArrayList<>();
        if (plannerDTO.getTicketIds() != null && !plannerDTO.getTicketIds().isEmpty()) {
            tickets = ticketRepository.findAllById(plannerDTO.getTicketIds());

            // Associa planner ai tickets
            for (Ticket ticket : tickets) {
                ticket.setPlanner(existingPlanner);
            }
        }
        existingPlanner.setTickets(tickets);

        // Gestione bookings
        List<Booking> bookings = new ArrayList<>();
        if (plannerDTO.getBookingIds() != null && !plannerDTO.getBookingIds().isEmpty()) {
            bookings = bookingRepository.findAllById(plannerDTO.getBookingIds());

            // Associa planner ai bookings
            for (Booking booking : bookings) {
                booking.setPlanner(existingPlanner);
            }
        }
        existingPlanner.setBookings(bookings);

        Planner updatedPlanner = plannerRepository.save(existingPlanner);

        return PlannerMapper.toDTO(updatedPlanner);
    }

    @Transactional
    public void deletePlanner(Long plannerId) {
        Planner planner = plannerRepository.findById(plannerId)
                .orElseThrow(() -> new ResourceNotFoundException("Planner not found with id: " + plannerId));

        plannerRepository.delete(planner);
    }

}
