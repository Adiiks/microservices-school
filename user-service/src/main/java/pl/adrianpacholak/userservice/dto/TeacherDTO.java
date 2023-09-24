package pl.adrianpacholak.userservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record TeacherDTO(
        @NotNull(message = "basicInfo object is required")
        @Valid
        UserDTO basicInfo,
        @Pattern(regexp = "\\d{9}", message = "Phone number must contain 9 digit")
        String phoneNumber,
        @NotNull(message = "Office number is required")
        @Positive(message = "Office number cannot be negative")
        Integer officeNumber
) {
}
