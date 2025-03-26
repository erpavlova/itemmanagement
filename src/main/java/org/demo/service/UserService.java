package org.demo.service;

import org.demo.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if ("testuser".equals(username)) {
            return new User("testuser", "{noop}password"); // Now using your custom User
        } else {
            throw new UsernameNotFoundException("User not found: " + username);
        }
    }
}
