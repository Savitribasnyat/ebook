package com.bookrentalsystem.bks.service.userdetailservice;

import com.bookrentalsystem.bks.model.login.CustomeUserDetails;
import com.bookrentalsystem.bks.model.login.User;
import com.bookrentalsystem.bks.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MyUserDetailService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
       Optional<User> user = userRepo.findUserByEmail(email);

        return user
                .map(CustomeUserDetails::new)   //user -> CustomeUserDetails(user) same
                .orElseThrow(()->new UsernameNotFoundException("Username does not exist!!!"));

    }
}
