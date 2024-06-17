package com.sumerge.auth.repository.boundary;

import com.sumerge.auth.repository.entity.UserDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends MongoRepository<UserDocument, UUID> {

    Optional<UserDocument> findByEmail(String email);

    UserDocument findByFirebaseId(String uid);
}
