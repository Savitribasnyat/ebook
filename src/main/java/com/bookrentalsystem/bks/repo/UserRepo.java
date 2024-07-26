package com.bookrentalsystem.bks.repo;

import com.bookrentalsystem.bks.model.login.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User,Long> {
    Optional<User> findUserByEmail(String email);
    @Query(nativeQuery = true, value = "select * from users where roles = 'USER'")
    List<User> findAllUsers();


}
