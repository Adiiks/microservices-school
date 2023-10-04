package pl.adrianpacholak.lessonservice.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.adrianpacholak.lessonservice.dto.*;
import pl.adrianpacholak.lessonservice.model.Registration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class RegistrationConverter {

    private final LessonConverter lessonConverter;

    public Registration registrationRequestToRegistration(RegistrationRequest request) {
        return Registration.builder()
                .beginDateTime(LocalDateTime.of(LocalDate.parse(request.beginDate()),
                        LocalTime.parse(request.beginTime())))
                .endDateTime(LocalDateTime.of(LocalDate.parse(request.endDate()),
                        LocalTime.parse(request.endTime())))
                .build();
    }

    public RegistrationResponse registrationToRegistrationResponse(Registration registration, TeacherResponse teacher,
                                                                   CourseResponse course, Boolean isUserSignUp) {
        LessonResponse lesson = lessonConverter.lessonToLessonResponse(registration.getLesson(), teacher, course);

        return new RegistrationResponse(registration.getId(), registration.getBeginDateTime(),
                registration.getEndDateTime(), lesson, isUserSignUp);
    }
}
