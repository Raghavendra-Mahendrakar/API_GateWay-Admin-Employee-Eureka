package com.pws.employee.security.jwt.config;

import com.pws.employee.entity.User;
import com.pws.employee.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @Author Vinayak M
 * @Date 05/01/23
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

            Optional<User> optionalUser = userRepository.findUserByEmail(username);
            if (optionalUser.isPresent()) {
                return optionalUser.get();
            }else
                throw new UsernameNotFoundException(username);
    }
}

