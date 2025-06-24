package com.petraccia.elisabetta.CretaceousPark.service;

import com.petraccia.elisabetta.CretaceousPark.dto.TicketDTO;
import com.petraccia.elisabetta.CretaceousPark.exception.BadRequestException;
import com.petraccia.elisabetta.CretaceousPark.exception.ResourceNotFoundException;
import com.petraccia.elisabetta.CretaceousPark.mapper.TicketMapper;
import com.petraccia.elisabetta.CretaceousPark.model.*;
import com.petraccia.elisabetta.CretaceousPark.repository.AttractionRepository;
import com.petraccia.elisabetta.CretaceousPark.repository.PlannerRepository;
import com.petraccia.elisabetta.CretaceousPark.repository.ShowRepository;
import com.petraccia.elisabetta.CretaceousPark.repository.TicketRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final PlannerRepository plannerRepository;
    private final ShowRepository showRepository;
    private final AttractionRepository attractionRepository;

    public TicketService(TicketRepository ticketRepository, PlannerRepository plannerRepository, ShowRepository showRepository, AttractionRepository attractionRepository) {
        this.ticketRepository = ticketRepository;
        this.plannerRepository = plannerRepository;
        this.showRepository = showRepository;
        this.attractionRepository = attractionRepository;
    }

    public List<TicketDTO> getAllShows() {
        return ticketRepository.findAll()
                .stream()
                .map(TicketMapper::toDTO)
                .toList();
    }

    public TicketDTO getTicketById(Long id) {
        if (id == null) {
            throw new BadRequestException("ID must not be null.");
        }

        Ticket show = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + id));

        return TicketMapper.toDTO(show);
    }

    public List<TicketDTO> findTicketsByAttractionIdOrShowId(Long attractionId, Long showId) {
        List<Ticket> tickets = new ArrayList<>();

        if (attractionId != null) {
            tickets = ticketRepository.findByAttractionId(attractionId);
        } else if (showId != null) {
            tickets = ticketRepository.findByShowId(showId);
        }

        return tickets.stream()
                .map(TicketMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<TicketDTO> getTicketsByPlannerId(Long plannerId) {
        if (plannerId == null) {
            throw new BadRequestException("Planner ID must not be null.");
        }

        Planner planner = plannerRepository.findById(plannerId)
                .orElseThrow(() -> new ResourceNotFoundException("Planner not found with id: " + plannerId));

        List<Ticket> tickets = ticketRepository.findByPlannerId(plannerId);

        if (tickets == null || tickets.isEmpty()) {
            throw new ResourceNotFoundException("No tickets found for Planner id: " + plannerId);
        }

        return tickets.stream()
                .map(TicketMapper::toDTO)
                .toList();
    }

    public TicketDTO saveTicket(TicketDTO ticketDTO) {

        if (ticketDTO.getAttractionId() != null && ticketDTO.getShowId() != null) {
            throw new BadRequestException("A ticket can be linked either to an attraction or to a show, not both.");
        }

        if (ticketDTO.getAttractionId() == null && ticketDTO.getShowId() == null) {
            throw new BadRequestException("A ticket must be linked to either an attraction or a show.");
        }

        Planner planner = null;

        if(ticketDTO.getPlannerId() != null) {
            planner = plannerRepository.findById(ticketDTO.getPlannerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Planner not found with id: " + ticketDTO.getPlannerId()));
        }

        Attraction attraction = null;
        Show show = null;

        if (ticketDTO.getAttractionId() != null) {
            attraction = attractionRepository.findById(ticketDTO.getAttractionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Attraction not found with id: " + ticketDTO.getAttractionId()));
        } else {
            show = showRepository.findById(ticketDTO.getShowId())
                    .orElseThrow(() -> new ResourceNotFoundException("Show not found with id: " + ticketDTO.getShowId()));
        }

        Ticket ticketToSave = TicketMapper.toEntity(ticketDTO, attraction, show, planner);
        Ticket savedTicket = ticketRepository.save(ticketToSave);
        return TicketMapper.toDTO(savedTicket);
    }

    @Transactional
    public void deleteTicketById(Long id) {
        if (id == null) {
            throw new BadRequestException("ID must not be null.");
        }

        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + id));

        ticketRepository.deleteById(id);
    }

    public TicketDTO updateTicket(Long id, TicketDTO ticketDTO) {
        if (id == null) {
            throw new BadRequestException("Ticket ID must not be null.");
        }
        if (ticketDTO == null) {
            throw new BadRequestException("Ticket data must not be null.");
        }

        Ticket existingTicket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + id));

        // Controllo: non possono essere presenti entrambi
        if (ticketDTO.getAttractionId() != null && ticketDTO.getShowId() != null) {
            throw new BadRequestException("Ticket must be for either an Attraction or a Show, not both.");
        }

        Planner planner = null;

        // Recupero il planner
        if(ticketDTO.getPlannerId() != null) {
            planner = plannerRepository.findById(ticketDTO.getPlannerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Planner not found with id: " + ticketDTO.getPlannerId()));
        }

        // Recupero opzionale Restaurant o Bookable
        Attraction attraction = null;
        Show show = null;

        if (ticketDTO.getAttractionId() != null) {
            attraction = attractionRepository.findById(ticketDTO.getAttractionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Attraction not found with id: " + ticketDTO.getAttractionId()));
        }

        if (ticketDTO.getShowId() != null) {
            show = showRepository.findById(ticketDTO.getShowId())
                    .orElseThrow(() -> new ResourceNotFoundException("Show not found with id: " + ticketDTO.getShowId()));
        }

        // Aggiornamento campi
        existingTicket.setDate(ticketDTO.getDate());
        existingTicket.setPrice(ticketDTO.getPrice());
        existingTicket.setSold(ticketDTO.isSold());
        existingTicket.setAttraction(attraction);
        existingTicket.setShow(show);
        existingTicket.setPlanner(planner);

        Ticket updatedTicket = ticketRepository.save(existingTicket);
        return TicketMapper.toDTO(updatedTicket);
    }

    @Transactional
    public TicketDTO buyTicket(Long ticketId, Long plannerId, Long customerId) throws AccessDeniedException {
        if (ticketId == null || plannerId == null || customerId == null) {
            throw new BadRequestException("Ticket ID, Planner ID and Customer ID must not be null.");
        }

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + ticketId));

        if (ticket.isSold()) {
            throw new BadRequestException("Ticket is already sold.");
        }

        Planner planner = plannerRepository.findById(plannerId)
                .orElseThrow(() -> new ResourceNotFoundException("Planner not found with id: " + plannerId));

        if (!planner.getCustomer().getId().equals(customerId)) {
            throw new AccessDeniedException("Customer does not own this planner.");
        }

        Customer customer = planner.getCustomer();
        Wallet wallet = customer.getWallet();

        if (wallet == null) {
            throw new ResourceNotFoundException("Wallet not found for customer with id: " + customerId);
        }

        BigDecimal price = ticket.getPrice();
        BigDecimal currentBalance = wallet.getBalance();

        if (currentBalance.compareTo(price) < 0) {
            throw new BadRequestException("Insufficient funds in wallet.");
        }

        // Sottraggo il prezzo al saldo wallet
        wallet.setBalance(currentBalance.subtract(price));

        // Associo il planner al ticket e lo segno come venduto
        ticket.setPlanner(planner);
        ticket.setSold(true);

        // Salvo le modifiche, se usi walletRepository salvalo esplicitamente (di solito in @Transactional si fa automaticamente)
        ticketRepository.save(ticket);

        return TicketMapper.toDTO(ticket);
    }


}
