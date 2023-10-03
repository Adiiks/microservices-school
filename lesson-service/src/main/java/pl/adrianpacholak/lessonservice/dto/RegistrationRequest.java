package pl.adrianpacholak.lessonservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record RegistrationRequest(
        @Pattern(regexp = "[2-9][0-9]{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])", message = "Valid date format: YYYY-MM-DD")
        String beginDate,
        @Pattern(regexp = "([0-1][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]", message = "Valid time format is: HH:MM:SS")
        String beginTime,
        @Pattern(regexp = "[2-9][0-9]{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])", message = "Valid date format: YYYY-MM-DD")
        String endDate,
        @Pattern(regexp = "([0-1][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]", message = "Valid time format is: HH:MM:SS")
        String endTime,
        @NotNull(message = "Lesson ID is required")
        Integer lessonId
) {
}
