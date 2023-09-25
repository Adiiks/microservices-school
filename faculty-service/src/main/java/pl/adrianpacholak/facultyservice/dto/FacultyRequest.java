package pl.adrianpacholak.facultyservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record FacultyRequest(
        @NotBlank
        String name,
        @NotBlank
        String address,
        @Pattern(regexp = "[0-9]{9}")
        String phoneNumber
) {
}
