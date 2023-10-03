package pl.adrianpacholak.lessonservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import pl.adrianpacholak.lessonservice.converter.RegistrationConverter;
import pl.adrianpacholak.lessonservice.dto.RegistrationRequest;
import pl.adrianpacholak.lessonservice.model.Lesson;
import pl.adrianpacholak.lessonservice.model.Registration;
import pl.adrianpacholak.lessonservice.repository.LessonRepository;
import pl.adrianpacholak.lessonservice.repository.RegistrationRepository;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final LessonRepository lessonRepository;
    private final RegistrationConverter registrationConverter;

    @Transactional
    public void openRegistration(RegistrationRequest request) {
        Lesson lessonDb = findLesson(request.lessonId());

        validateBeginDateAndEndDate(request.beginDate(), request.endDate());

        Registration registration = registrationConverter.registrationRequestToRegistration(request);
        registration.setLesson(lessonDb);

        registrationRepository.save(registration);
    }

    private Lesson findLesson(Integer lessonId) {
        return lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Lesson with ID: " + lessonId + " not found."));
    }

    private void validateBeginDateAndEndDate(String beginDate, String endDate) {
        LocalDate begin = LocalDate.parse(beginDate);
        LocalDate end = LocalDate.parse(endDate);

        if (begin.isAfter(end) || begin.isEqual(end)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Begin date have to be before end date");
        }
    }
}
