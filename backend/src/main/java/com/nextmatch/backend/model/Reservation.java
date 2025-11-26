package com.nextmatch.backend.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

@Document(collection = "reservations")
public class Reservation {

    @Id
    private String id;
    private String cancha;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private String equipoId;
    private String jugadorId;
    private String fieldId;
    private ReservationStatus estado = ReservationStatus.PENDIENTE;
    private String notas;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    public Reservation() {
    }

    public Reservation(String id, String cancha, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin,
                       String equipoId, String jugadorId, ReservationStatus estado, String notas, String fieldId) {
        this.id = id;
        this.cancha = cancha;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.equipoId = equipoId;
        this.jugadorId = jugadorId;
        this.estado = estado;
        this.notas = notas;
        this.fieldId = fieldId;
    }

    // getters/setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
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
    public String getFieldId() { return fieldId; }
    public void setFieldId(String fieldId) { this.fieldId = fieldId; }
    public ReservationStatus getEstado() { return estado; }
    public void setEstado(ReservationStatus estado) { this.estado = estado; }
    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
