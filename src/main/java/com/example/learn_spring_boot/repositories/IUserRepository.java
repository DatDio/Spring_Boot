package com.example.learn_spring_boot.repositories;

import com.example.learn_spring_boot.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<Users,Long>, JpaSpecificationExecutor<Users> {


}
