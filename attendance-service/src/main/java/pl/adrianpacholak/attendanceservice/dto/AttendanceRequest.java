package pl.adrianpacholak.attendanceservice.dto;

import jakarta.validation.constraints.NotNull;
import pl.adrianpacholak.attendanceservice.model.EStatus;

public record AttendanceRequest(
        @NotNull(message = "Student ID is required")
        Integer studentId,
        @NotNull(message = "Status is required. Possible values are: PRESENT, JUSTIFIED_ABSENCE, ABSENCE")
        EStatus status,
        String comment
) {
}
