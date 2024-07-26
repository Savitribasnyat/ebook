package com.bookrentalsystem.bks.repo;

import com.bookrentalsystem.bks.model.ForgotPassword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ForgotPasswordRepo extends JpaRepository<ForgotPassword,Long> {
    Optional<ForgotPassword> findByCode(Integer code);
}
