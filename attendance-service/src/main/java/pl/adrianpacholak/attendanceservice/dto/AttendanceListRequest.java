package pl.adrianpacholak.attendanceservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

public record AttendanceListRequest(
        @NotNull(message = "Lesson ID is required")
        Integer lessonId,
        @Pattern(regexp = "[2-9][0-9]{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])", message = "Valid date format: YYYY-MM-DD")
        String date,
        @Size(min = 1, message = "Must be minimum one attendance")
        @Valid
        List<AttendanceRequest> attendancies
) {
}
