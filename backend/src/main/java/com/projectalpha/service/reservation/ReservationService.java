/*package com.projectalpha.service.reservation;

import com.projectalpha.dto.reservation.ReservationDto;
import com.projectalpha.dto.reservation.ReservationStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReservationService {
    ReservationDTO createReservation(ReservationDTO reservationDTO);
    Optional<ReservationDTO> getReservationById(UUID id);
    List<ReservationDTO> getReservationsByBusiness(UUID businessId);
    List<ReservationDTO> getReservationsByUser(String userId);
    List<ReservationDTO> getReservationsByBusinessAndTimeRange(UUID businessId, LocalDateTime start, LocalDateTime end);
    List<ReservationDTO> getReservationsByStatus(ReservationStatus status);
    void updateReservationStatus(UUID id, ReservationStatus status);
    void cancelReservation(UUID id);
}*/