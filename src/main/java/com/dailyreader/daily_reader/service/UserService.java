package com.dailyreader.daily_reader.service;

import com.dailyreader.daily_reader.dto.UserRequest;
import com.dailyreader.daily_reader.dto.UserResponse;
import com.dailyreader.daily_reader.entity.User;
import com.dailyreader.daily_reader.exception.BadRequestException;
import com.dailyreader.daily_reader.exception.ErrorMessages;
import static com.dailyreader.daily_reader.exception.ExceptionHandler.throwIf;
import com.dailyreader.daily_reader.repository.UserRepository;
import org.springframework.stereotype.Service;



@Service
public class UserService {

    private final UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse registerUser(UserRequest userRequest) {

        boolean isExist = userRepository.existsByEmail(userRequest.email());

       throwIf(isExist,
               () -> new BadRequestException(ErrorMessages.EMAIL_ALREADY_EXISTS));

        User user = User.builder()
                .email(userRequest.email())
                .userName(userRequest.userName())
                .build();

        userRepository.save(user);

        return new UserResponse(
                user.getEmail(),
                user.getUserName()
        );

    }


}
