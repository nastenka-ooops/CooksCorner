package com.example.cooks_corner.service;

import com.example.cooks_corner.dto.UserDto;
import com.example.cooks_corner.entity.AppUser;
import com.example.cooks_corner.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private final AppUserRepository userRepository;

    @Autowired
    public UserService(AppUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public AppUser loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmailIgnoreCase(username).orElseThrow(() ->
                new UsernameNotFoundException("user not found"));
    }

    public AppUser findByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email).orElseThrow(() ->
                new UsernameNotFoundException("user not found"));
    }

    public UserDto getUser(String email) {
        AppUser user = loadUserByUsername(email);
        return new UserDto(user.getName(), user.getBio(), user.getEmail(), user.getImage().getUrl());
    }

    public void confirmUser(AppUser user) {
        user.setEnabled(true);
        userRepository.save(user);
    }

    public void deleteAllUsers(){
        userRepository.deleteAll();
    }
}
