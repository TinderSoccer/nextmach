package com.nextmatch.backend.repository;

import com.nextmatch.backend.model.Player;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PlayerRepository extends MongoRepository<Player, String> {
    List<Player> findByEquipoId(String equipoId);
}
