package com.nextmatch.backend.controller;

import com.nextmatch.backend.model.Team;
import com.nextmatch.backend.repository.TeamRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    private final TeamRepository teamRepository;

    public TeamController(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @GetMapping
    public List<Team> getTeams() {
        return teamRepository.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Team createTeam(@Valid @RequestBody TeamRequest request) {
        return teamRepository.save(request.toEntity());
    }

    @GetMapping("/{id}")
    public Team getTeam(@PathVariable String id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Equipo no encontrado"));
    }

    @PutMapping("/{id}")
    public Team updateTeam(@PathVariable String id, @Valid @RequestBody TeamRequest request) {
        Team existing = getTeam(id);
        existing.setNombre(request.getNombre());
        existing.setCiudad(request.getCiudad());
        existing.setNivel(request.getNivel());
        existing.setContacto(request.getContacto());
        existing.setDescripcion(request.getDescripcion());
        return teamRepository.save(existing);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTeam(@PathVariable String id) {
        Team existing = getTeam(id);
        teamRepository.delete(existing);
    }

    static class TeamRequest {
        @NotBlank
        private String nombre;
        @NotBlank
        private String ciudad;
        @NotBlank
        private String nivel;
        @NotBlank
        private String contacto;
        private String descripcion;

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public String getCiudad() { return ciudad; }
        public void setCiudad(String ciudad) { this.ciudad = ciudad; }
        public String getNivel() { return nivel; }
        public void setNivel(String nivel) { this.nivel = nivel; }
        public String getContacto() { return contacto; }
        public void setContacto(String contacto) { this.contacto = contacto; }
        public String getDescripcion() { return descripcion; }
        public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

        Team toEntity() {
            return new Team(null, nombre, ciudad, nivel, contacto, descripcion);
        }
    }
}
