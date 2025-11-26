package com.nextmatch.backend.repository;

import com.nextmatch.backend.model.Reservation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends MongoRepository<Reservation, String> {
    List<Reservation> findByEquipoId(String equipoId);
    List<Reservation> findByFecha(LocalDate fecha);
}
