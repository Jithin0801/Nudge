package com.jithin.nudge.service;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jithin.nudge.dto.UserRegistrationDTO;
import com.jithin.nudge.dto.UserSuccessResponseDTO;
import com.jithin.nudge.entity.UserPersonal;
import com.jithin.nudge.entity.UserSecurity;
import com.jithin.nudge.exception.UserAlreadyPresentException;
import com.jithin.nudge.repository.UserSecurityRepository;

@Service
@Transactional
public class UserService implements UserDetailsService {

    private UserSecurityRepository userSecurityRepository;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    UserService(UserSecurityRepository userSecurityRepository, org.springframework.security.crypto.password.PasswordEncoder passwordEncoder) {
        this.userSecurityRepository = userSecurityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserSuccessResponseDTO registerUser(UserRegistrationDTO userRegistrationDTO) {
        UserSecurity isUserSecurityPresent = this.userSecurityRepository.findUserByEmailId(userRegistrationDTO.getEmailId());

        if (isUserSecurityPresent != null) {
            throw new UserAlreadyPresentException("User with email " + userRegistrationDTO.getEmailId() + " already exists");
        }

        UserSecurity userSecurity = new UserSecurity();
        UserPersonal userPersonal = new UserPersonal();

        userSecurity.setEmailId(userRegistrationDTO.getEmailId());
        userSecurity.setPassword(passwordEncoder.encode(userRegistrationDTO.getPassword()));

        userPersonal.setFirstName(userRegistrationDTO.getFirstName());
        userPersonal.setLastName(userRegistrationDTO.getLastName());
        userPersonal.setDateOfBirth(userRegistrationDTO.getDateOfBirth());
        userPersonal.setPhoneNumber(userRegistrationDTO.getPhoneNumber());

        userSecurity.setUserPersonal(userPersonal);
        userPersonal.setUserSecurity(userSecurity);

        UserSecurity savedUser = this.userSecurityRepository.save(userSecurity);

        return UserSuccessResponseDTO.builder().userId(savedUser.getUserId()).firstName(userPersonal.getFirstName()).lastName(userPersonal.getLastName()).email(savedUser.getEmailId())
                .phoneNumber(userPersonal.getPhoneNumber()).dateOfBirth(userPersonal.getDateOfBirth()).enabled(savedUser.isEnabled()).build();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserSecurity userSecurity = userSecurityRepository.findUserByEmailId(username);
        if (userSecurity == null) {
            throw new UsernameNotFoundException("User with " + username + " not found");
        }

        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ADMIN"));

        return new User(userSecurity.getEmailId(), userSecurity.getPassword(), authorities);
    }
}
