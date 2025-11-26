package com.nextmatch.backend.repository;

import com.nextmatch.backend.model.Field;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FieldRepository extends MongoRepository<Field, String> {
}
