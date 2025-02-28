package com.example.learn_spring_boot.repositories;

import com.example.learn_spring_boot.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository  extends JpaRepository<Users,Long> {
    Optional<Users> findByUserName(String userName);
}
