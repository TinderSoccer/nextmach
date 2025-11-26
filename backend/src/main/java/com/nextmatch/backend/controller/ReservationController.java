package com.nextmatch.backend.controller;

import com.nextmatch.backend.model.Reservation;
import com.nextmatch.backend.model.ReservationStatus;
import com.nextmatch.backend.repository.ReservationRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationRepository reservationRepository;

    public ReservationController(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @GetMapping
    public List<Reservation> getReservations(
            @RequestParam(required = false) String equipoId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha
    ) {
        if (equipoId != null) {
            return reservationRepository.findByEquipoId(equipoId);
        }
        if (fecha != null) {
            return reservationRepository.findByFecha(fecha);
        }
        return reservationRepository.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Reservation createReservation(@Valid @RequestBody ReservationRequest request) {
        return reservationRepository.save(request.toEntity());
    }

    @GetMapping("/{id}")
    public Reservation getReservation(@PathVariable String id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reserva no encontrada"));
    }

    @PutMapping("/{id}")
    public Reservation updateReservation(@PathVariable String id, @Valid @RequestBody ReservationRequest request) {
        Reservation existing = getReservation(id);
        existing.setCancha(request.getCancha());
        existing.setFecha(request.getFecha());
        existing.setHoraInicio(request.getHoraInicio());
        existing.setHoraFin(request.getHoraFin());
        existing.setEquipoId(request.getEquipoId());
        existing.setJugadorId(request.getJugadorId());
        existing.setEstado(request.getEstado());
        existing.setNotas(request.getNotas());
        return reservationRepository.save(existing);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReservation(@PathVariable String id) {
        Reservation existing = getReservation(id);
        reservationRepository.delete(existing);
    }

    static class ReservationRequest {
        @NotBlank
        private String cancha;
        @NotNull
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        private LocalDate fecha;
        @NotNull
        @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
        private LocalTime horaInicio;
        @NotNull
        @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
        private LocalTime horaFin;
        @NotBlank
        private String equipoId;
        private String jugadorId;
        private ReservationStatus estado = ReservationStatus.PENDIENTE;
        private String notas;

        // getters and setters
        public String getCancha() { return cancha; }
        public void setCancha(String cancha) { this.cancha = cancha; }
        public LocalDate getFecha() { return fecha; }
        public void setFecha(LocalDate fecha) { this.fecha = fecha; }
        public LocalTime getHoraInicio() { return horaInicio; }
        public void setHoraInicio(LocalTime horaInicio) { this.horaInicio = horaInicio; }
        public LocalTime getHoraFin() { return horaFin; }
        public void setHoraFin(LocalTime horaFin) { this.horaFin = horaFin; }
        public String getEquipoId() { return equipoId; }
        public void setEquipoId(String equipoId) { this.equipoId = equipoId; }
        public String getJugadorId() { return jugadorId; }
        public void setJugadorId(String jugadorId) { this.jugadorId = jugadorId; }
        public ReservationStatus getEstado() { return estado; }
        public void setEstado(ReservationStatus estado) { this.estado = estado; }
        public String getNotas() { return notas; }
        public void setNotas(String notas) { this.notas = notas; }

        Reservation toEntity() {
            return new Reservation(
                    null,
                    cancha,
                    fecha,
                    horaInicio,
                    horaFin,
                    equipoId,
                    jugadorId,
                    estado,
                    notas
            );
        }
    }
}
