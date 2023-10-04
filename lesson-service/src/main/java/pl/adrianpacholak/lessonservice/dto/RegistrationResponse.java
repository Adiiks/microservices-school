package pl.adrianpacholak.lessonservice.dto;

import java.time.LocalDateTime;

public record RegistrationResponse(
        Integer id,
        LocalDateTime beginDateTime,
        LocalDateTime endDateTime,
        LessonResponse lesson,
        Boolean isStudentRegister
) {
}
