package com.nextmatch.backend.controller;

import com.nextmatch.backend.model.Player;
import com.nextmatch.backend.repository.PlayerRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    private final PlayerRepository playerRepository;

    public PlayerController(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @GetMapping
    public List<Player> getPlayers(@RequestParam(required = false) String equipoId) {
        if (equipoId != null) {
            return playerRepository.findByEquipoId(equipoId);
        }
        return playerRepository.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Player createPlayer(@Valid @RequestBody PlayerRequest request) {
        return playerRepository.save(request.toEntity());
    }

    @GetMapping("/{id}")
    public Player getPlayer(@PathVariable String id) {
        return playerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Jugador no encontrado"));
    }

    @PutMapping("/{id}")
    public Player updatePlayer(@PathVariable String id, @Valid @RequestBody PlayerRequest request) {
        Player existing = getPlayer(id);
        existing.setNombre(request.getNombre());
        existing.setCorreo(request.getCorreo());
        existing.setPosicion(request.getPosicion());
        existing.setNivel(request.getNivel());
        existing.setTelefono(request.getTelefono());
        existing.setEquipoId(request.getEquipoId());
        existing.setActivo(request.isActivo());
        return playerRepository.save(existing);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePlayer(@PathVariable String id) {
        Player existing = getPlayer(id);
        playerRepository.delete(existing);
    }

    static class PlayerRequest {
        @NotBlank
        private String nombre;
        @Email
        private String correo;
        @NotBlank
        private String posicion;
        @NotBlank
        private String nivel;
        private String telefono;
        private String equipoId;
        private boolean activo = true;

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public String getCorreo() { return correo; }
        public void setCorreo(String correo) { this.correo = correo; }
        public String getPosicion() { return posicion; }
        public void setPosicion(String posicion) { this.posicion = posicion; }
        public String getNivel() { return nivel; }
        public void setNivel(String nivel) { this.nivel = nivel; }
        public String getTelefono() { return telefono; }
        public void setTelefono(String telefono) { this.telefono = telefono; }
        public String getEquipoId() { return equipoId; }
        public void setEquipoId(String equipoId) { this.equipoId = equipoId; }
        public boolean isActivo() { return activo; }
        public void setActivo(boolean activo) { this.activo = activo; }

        Player toEntity() {
            return new Player(null, nombre, correo, posicion, nivel, telefono, equipoId, activo);
        }
    }
}
