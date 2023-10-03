package pl.adrianpacholak.lessonservice.converter;

import org.springframework.stereotype.Component;
import pl.adrianpacholak.lessonservice.dto.RegistrationRequest;
import pl.adrianpacholak.lessonservice.model.Registration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
public class RegistrationConverter {

    public Registration registrationRequestToRegistration(RegistrationRequest request) {
        return Registration.builder()
                .beginDateTime(LocalDateTime.of(LocalDate.parse(request.beginDate()),
                        LocalTime.parse(request.beginTime())))
                .endDateTime(LocalDateTime.of(LocalDate.parse(request.endDate()),
                        LocalTime.parse(request.endTime())))
                .build();
    }
}
