package pl.adrianpacholak.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.pl.PESEL;

public record UserDTO(
        @NotBlank(message = "PESEL is required")
        @PESEL(message = "Invalid PESEL")
        String pesel,
        @NotBlank(message = "Name is required")
        String names,
        @NotBlank(message = "Lastname is required")
        String lastname,
        @NotNull(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,
        @NotBlank(message = "Password is required")
        String password
) {
}
