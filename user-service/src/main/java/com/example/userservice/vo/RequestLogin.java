package com.example.userservice.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class RequestLogin {
    @NotNull(message = "Email cannot be null")
    @Size(min=2, message = "Email not be less than two characters")
    @Email
    private String email;

    @NotNull(message = "Password cannot be null")
    @Size(min=8, message = "password must be equal or greater than eight characters")
    private String password;
}