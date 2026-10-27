package com.fitness.userservice.service;

import com.fitness.userservice.dto.request.RegisterRequest;
import com.fitness.userservice.dto.response.UserResponse;
import com.fitness.userservice.entity.UserEntity;
import com.fitness.userservice.repository.UserRepository;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public UserResponse getUserProfile(String userId) {
        UserEntity userFound = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

//        if(userFound.isEmpty()){
//            throw new RuntimeException("User Not found of id: "+ userId);
//        }

        UserResponse userResponse = new UserResponse();
        userResponse.setId(userFound.getId());
        userResponse.setEmail(userFound.getEmail());
        userResponse.setFirstName(userFound.getFirstName());
        userResponse.setLastName(userFound.getLastName());
        userResponse.setCreatedAt(userFound.getCreatedAt());
        userResponse.setUpdatedAt(userFound.getUpdatedAt());

        return userResponse;
    }

    public UserResponse register(RegisterRequest request) {
//        Optional<UserEntity> userFound = userRepository.findByEmail(request.getEmail());
//
//        if(userFound.isPresent()){
//            throw new RuntimeException("Email already exists!");
//        }

        if(userRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("Email already exists!");
        }

        UserEntity user = new UserEntity();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        UserEntity savedUser = userRepository.save(user);

        UserResponse userResponse = new UserResponse();
        userResponse.setId(savedUser.getId());
        userResponse.setEmail(savedUser.getEmail());
        userResponse.setFirstName(savedUser.getFirstName());
        userResponse.setLastName(savedUser.getLastName());
        userResponse.setCreatedAt(savedUser.getCreatedAt());
        userResponse.setUpdatedAt(savedUser.getUpdatedAt());

        return userResponse;
    }
}
