package com.bookrentalsystem.bks.service;

import com.bookrentalsystem.bks.dto.login.LoginUserDto;
import com.bookrentalsystem.bks.model.login.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User saveUser(LoginUserDto userDto);
    String findByEmail(String email);
    Optional<User> findUsingEmail(String email);
    User saveChangeUser(User user);
    User findById(Long id);

    String saveUserEntity(User user);
    List<User> findAllUser();

    void deleteById(Long id);

}
