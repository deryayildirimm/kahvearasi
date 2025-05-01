package com.dailyreader.daily_reader.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


public record UserRequest(
     @NotBlank
     @Email
     String email,

     @NotBlank
     String userName
) {

}
