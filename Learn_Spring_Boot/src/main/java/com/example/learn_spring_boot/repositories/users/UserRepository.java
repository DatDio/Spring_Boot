package com.example.learn_spring_boot.repositories.users;

import com.example.learn_spring_boot.dtos.users.UserDto;
import com.example.learn_spring_boot.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users,Long>,
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

    Optional<Users> findByUserName(String userName);

    Optional<Users> findByEmail(String email);
    boolean existsByEmail(String email);

    boolean existsByUserName(String userName);
}
