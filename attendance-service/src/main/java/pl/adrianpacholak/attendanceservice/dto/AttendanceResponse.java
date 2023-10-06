package pl.adrianpacholak.attendanceservice.dto;

import pl.adrianpacholak.attendanceservice.model.EStatus;

public record AttendanceResponse(
        Integer id,
        StudentResponse student,
        EStatus status,
        String comment
) {
}
