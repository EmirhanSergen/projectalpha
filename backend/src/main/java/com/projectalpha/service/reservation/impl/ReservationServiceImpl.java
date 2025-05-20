/*package com.projectalpha.service.reservation.impl;

import com.projectalpha.dto.reservation.ReservationDto;
import com.projectalpha.dto.reservation.ReservationStatus;
import com.projectalpha.repository.reservation.ReservationRepository;
import com.projectalpha.service.reservation.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationServiceImpl(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public ReservationDTO createReservation(ReservationDTO reservationDTO) {
        return reservationRepository.save(reservationDTO);
    }

    @Override
    public Optional<ReservationDto> getReservationById(UUID id) {
        return reservationRepository.findById(id);
    }

    @Override
    public List<ReservationDto> getReservationsByBusiness(UUID businessId) {
        return reservationRepository.findByBusinessId(businessId);
    }

    @Override
    public List<ReservationDTO> getReservationsByUser(String userId) {
        return reservationRepository.findByUserId(userId);
    }

    @Override
    public List<ReservationDTO> getReservationsByBusinessAndTimeRange(UUID businessId, LocalDateTime start, LocalDateTime end) {
        return reservationRepository.findByBusinessIdAndReservationTimeBetween(businessId, start, end);
    }

    @Override
    public List<ReservationDTO> getReservationsByStatus(ReservationStatus status) {
        return reservationRepository.findByStatus(status);
    }

    @Override
    public void updateReservationStatus(UUID id, ReservationStatus status) {
        reservationRepository.updateStatus(id, status);
    }

    @Override
    public void cancelReservation(UUID id) {
        reservationRepository.updateStatus(id, ReservationStatus.CANCELLED);
    }
}*/