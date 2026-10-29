package com.fitness.userservice.service;

import com.fitness.userservice.dto.request.RegisterRequest;
import com.fitness.userservice.dto.response.UserResponse;
import com.fitness.userservice.entity.UserEntity;
import com.fitness.userservice.exception.DuplicateResourceException;
import com.fitness.userservice.exception.ResourceNotFoundException;
import com.fitness.userservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public UserResponse getUserProfile(String userId) {
        UserEntity userFound = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

//        if(userFound.isEmpty()){
//            throw new RuntimeException("User Not found of id: "+ userId);
//        }

        return mapToDto(userFound);
    }

    public UserResponse register(RegisterRequest request) {
//        Optional<UserEntity> userFound = userRepository.findByEmail(request.getEmail());
//
//        if(userFound.isPresent()){
//            throw new RuntimeException("Email already exists!");
//        }

        if(userRepository.existsByEmail(request.getEmail())){
            throw new DuplicateResourceException("Email already exists!");
        }

        UserEntity user = new UserEntity();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        UserEntity savedUser = userRepository.save(user);

        return mapToDto(savedUser);
    }

    private UserResponse mapToDto(UserEntity user){
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setEmail(user.getEmail());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setCreatedAt(user.getCreatedAt());
        userResponse.setUpdatedAt(user.getUpdatedAt());
        return  userResponse;
    }

    public Boolean existsByUserId(String userId) {
        log.info("Calling User Validation Api for userId: {} ", userId);
        return userRepository.existsById(userId);
    }
}
