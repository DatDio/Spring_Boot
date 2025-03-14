package com.example.learn_spring_boot.repositories.users;

import com.example.learn_spring_boot.Utils.ApiResponse;
import com.example.learn_spring_boot.dtos.requests.users.SearchUserRequest;
import com.example.learn_spring_boot.dtos.requests.users.UserDto;
import com.example.learn_spring_boot.entities.Users;
import com.example.learn_spring_boot.enums.Gender;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.security.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public interface IUserRepository extends JpaRepository<Users,Long>,
        JpaSpecificationExecutor<Users> {
//    @Procedure(name = "search_users")
//    List<Object[]> searchUserProcedure(
//            @Param("p_userName") String userName,
//            @Param("p_email") String email,
//            @Param("p_phoneNumber") String phoneNumber,
//            @Param("p_gender") String gender,
//            @Param("p_dateFrom") LocalDate dateFrom,
//            @Param("p_dateTo") LocalDate dateTo
//    );

@Procedure(name = "search_users")
    List<UserDto> getAllUsers();
}
