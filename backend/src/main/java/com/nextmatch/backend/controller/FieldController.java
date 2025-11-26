package com.nextmatch.backend.controller;

import com.nextmatch.backend.model.Field;
import com.nextmatch.backend.repository.FieldRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/fields")
public class FieldController {

    private final FieldRepository fieldRepository;

    public FieldController(FieldRepository fieldRepository) {
        this.fieldRepository = fieldRepository;
    }

    @GetMapping
    public List<Field> getFields() {
        return fieldRepository.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Field createField(@Valid @RequestBody FieldRequest request) {
        return fieldRepository.save(request.toEntity());
    }

    @GetMapping("/{id}")
    public Field getField(@PathVariable String id) {
        return fieldRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cancha no encontrada"));
    }

    @PutMapping("/{id}")
    public Field updateField(@PathVariable String id, @Valid @RequestBody FieldRequest request) {
        Field existing = getField(id);
        existing.setNombre(request.getNombre());
        existing.setDireccion(request.getDireccion());
        existing.setLatitud(request.getLatitud());
        existing.setLongitud(request.getLongitud());
        existing.setDescripcion(request.getDescripcion());
        existing.setServicios(request.getServicios());
        return fieldRepository.save(existing);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteField(@PathVariable String id) {
        Field existing = getField(id);
        fieldRepository.delete(existing);
    }

    static class FieldRequest {
        @NotBlank
        private String nombre;
        @NotBlank
        private String direccion;
        @NotNull
        private Double latitud;
        @NotNull
        private Double longitud;
        private String descripcion;
        private List<String> servicios;

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public String getDireccion() { return direccion; }
        public void setDireccion(String direccion) { this.direccion = direccion; }
        public Double getLatitud() { return latitud; }
        public void setLatitud(Double latitud) { this.latitud = latitud; }
        public Double getLongitud() { return longitud; }
        public void setLongitud(Double longitud) { this.longitud = longitud; }
        public String getDescripcion() { return descripcion; }
        public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
        public List<String> getServicios() { return servicios; }
        public void setServicios(List<String> servicios) { this.servicios = servicios; }

        Field toEntity() {
            return new Field(null, nombre, direccion, latitud, longitud, descripcion, servicios);
        }
    }
}
