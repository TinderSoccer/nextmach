package com.nextmatch.backend.repository;

import com.nextmatch.backend.model.Team;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TeamRepository extends MongoRepository<Team, String> {
}
