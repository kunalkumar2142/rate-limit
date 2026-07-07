package com.ratelimit.Service;

import com.ratelimit.Model.User;
import com.ratelimit.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUser(String name) {
        return userRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("User not found: " + name));
    }

}
