package com.socialmedia.serviceimpl.repository;

import com.socialmedia.serviceimpl.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {
    Boolean existsByName(String name);
    Boolean existsByEmail(String email);
    Optional<User> findUserByEmail(String email);

}
