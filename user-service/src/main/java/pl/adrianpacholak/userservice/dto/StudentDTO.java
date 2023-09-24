package pl.adrianpacholak.userservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record StudentDTO(
        @NotNull(message = "basicInfo object is required")
        @Valid
        UserDTO basicInfo
) {
}
