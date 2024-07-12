package com.example.cooks_corner.service;

import com.example.cooks_corner.dto.LoginRequest;
import com.example.cooks_corner.dto.LoginResponse;
import com.example.cooks_corner.dto.PasswordUpdateRequest;
import com.example.cooks_corner.dto.RegistrationRequest;
import com.example.cooks_corner.entity.AppUser;
import com.example.cooks_corner.entity.Role;
import com.example.cooks_corner.entity.enums.RoleEnum;
import com.example.cooks_corner.exception.EmailAlreadyTakenException;
import com.example.cooks_corner.exception.InvalidRegistrationRequestException;
import com.example.cooks_corner.repository.AppUserRepository;
import com.example.cooks_corner.repository.RoleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import java.util.HashSet;
import java.util.Optional;

@Service
public class AuthenticationService {
    private final AppUserRepository userRepository;
    private final RoleRepository roleRepository;
    private final Validator validator;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UserService userService;
    private final MailService mailService;
    private final ImageService imageService;
    private final ObjectMapper objectMapper;


    @Autowired
    public AuthenticationService(AppUserRepository userRepository, RoleRepository roleRepository, Validator validator, PasswordEncoder passwordEncoder,
                                 AuthenticationManager authenticationManager, TokenService tokenService, UserService userService, MailService mailService, ImageService imageService, ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.validator = validator;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.userService = userService;
        this.mailService = mailService;
        this.imageService = imageService;
        this.objectMapper = objectMapper;
    }

    public void createUser(RegistrationRequest registrationRequest) {
        validateRegistrationRequest(registrationRequest);

        if (emailExists(registrationRequest.email())) {
            throw new EmailAlreadyTakenException("Login is already taken");
        }

        AppUser user = new AppUser(registrationRequest.name(), registrationRequest.email(),
                passwordEncoder.encode(registrationRequest.password()), new HashSet<>());

        Optional<Role> role = roleRepository.findByAuthority(RoleEnum.USER);
        role.ifPresent(value -> user.getRoles().add(value));

        userRepository.save(user);

        mailService.sendConfirmation(user.getEmail());
    }

    public LoginResponse loginUser(LoginRequest loginRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.email(), loginRequest.password()));
        } catch (DisabledException e) {
            throw new DisabledException("User is disable");
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("This email does not exist");
        }
        AppUser user = (AppUser) authentication.getPrincipal();

        String accessToken = tokenService.generateAccessToken(user);
        String refreshToken = tokenService.generateRefreshToken(user);

        return new LoginResponse(loginRequest.email(),
                accessToken, refreshToken);

    }

    public boolean emailExists(String email) {
        return userRepository.findByEmailIgnoreCase(email).isPresent();
    }

    public LoginResponse confirmEmail(String token) {
        Jwt decodedToken = tokenService.decodeToken(token);
        if (decodedToken != null) {
            String email = decodedToken.getSubject();
            AppUser user = userService.loadUserByUsername(email);
            if (user != null && !user.getEnabled()) {
                userService.confirmUser(user);

                String accessToken = tokenService.generateAccessToken(user);
                String refreshToken = tokenService.generateRefreshToken(user);

                return new LoginResponse(user.getEmail(), accessToken, refreshToken);
            } else {
                throw new EmailAlreadyTakenException("The user was not found or has already been verified.");
            }
        } else {
            throw new InvalidRegistrationRequestException("Invalid or expired token.");
        }
    }

    private void validateRegistrationRequest(RegistrationRequest request) {
        BindingResult bindingResult = new BeanPropertyBindingResult(request, "registrationRequest");
        validator.validate(request, bindingResult);

        if (bindingResult.hasErrors()) {
            throw new InvalidRegistrationRequestException("Invalid registration request " + bindingResult.getAllErrors());
        }
    }

    public void forgotPassword(String email) {
        Optional<AppUser> user = userRepository.findByEmailIgnoreCase(email);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("Email address not found.");
        }

        mailService.sendPasswordResetEmail(email);
    }

    public String updatePassword(String token, PasswordUpdateRequest request) {
        Jwt decodedToken = tokenService.decodeToken(token);

        if (decodedToken != null) {
            String email = decodedToken.getSubject();
            AppUser user = userService.loadUserByUsername(email);
            if (user != null) {
                user.setPassword(passwordEncoder.encode(request.password()));
                userRepository.save(user);
                return "Password updated! You can now log in.";
            } else {
                return "The user was not found";
            }
        } else {
            return "Invalid or expired token.";
        }
    }

}
