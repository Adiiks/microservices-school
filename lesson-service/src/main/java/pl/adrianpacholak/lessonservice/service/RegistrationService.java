package pl.adrianpacholak.lessonservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import pl.adrianpacholak.lessonservice.client.CourseClient;
import pl.adrianpacholak.lessonservice.client.UserClient;
import pl.adrianpacholak.lessonservice.converter.RegistrationConverter;
import pl.adrianpacholak.lessonservice.dto.CourseResponse;
import pl.adrianpacholak.lessonservice.dto.RegistrationRequest;
import pl.adrianpacholak.lessonservice.dto.RegistrationResponse;
import pl.adrianpacholak.lessonservice.dto.TeacherResponse;
import pl.adrianpacholak.lessonservice.model.Lesson;
import pl.adrianpacholak.lessonservice.model.Registration;
import pl.adrianpacholak.lessonservice.repository.LessonRepository;
import pl.adrianpacholak.lessonservice.repository.RegistrationRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final LessonRepository lessonRepository;
    private final RegistrationConverter registrationConverter;
    private final UserClient userClient;
    private final CourseClient courseClient;

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

    public Page<RegistrationResponse> getRegistrations(String username, Pageable pageable) {
        Page<Registration> registrationPageDb = registrationRepository.findAll(pageable);

        List<Integer> teachersIds = new ArrayList<>();
        List<Integer> coursesIds = new ArrayList<>();
        Map<Integer, Boolean> isUserRegisteredToLesson = new HashMap<>();

        registrationPageDb.getContent().forEach(registration -> {
            teachersIds.add(registration.getLesson().getTeacherId());
            coursesIds.add(registration.getLesson().getCourseId());
            isUserRegisteredToLesson.put(registration.getLesson().getId(),
                    registration.getLesson().isStudentSignUp(username));
        });

        Map<Integer, TeacherResponse> teachers = getTeachersByIds(teachersIds);
        Map<Integer, CourseResponse> courses = getCoursesByIds(coursesIds);

        List<RegistrationResponse> registrations = registrationPageDb.get()
                .map(registration -> registrationConverter.registrationToRegistrationResponse(registration,
                        teachers.get(registration.getLesson().getTeacherId()),
                        courses.get(registration.getLesson().getCourseId()),
                        isUserRegisteredToLesson.get(registration.getLesson().getId())))
                .toList();

        return new PageImpl<>(registrations, registrationPageDb.getPageable(), registrationPageDb.getTotalElements());
    }

    private Map<Integer, TeacherResponse> getTeachersByIds(List<Integer> ids) {
        return userClient.getTeachersByIds(ids)
                .stream()
                .collect(Collectors.toMap(TeacherResponse::id, teacherResponse -> teacherResponse));
    }

    private Map<Integer, CourseResponse> getCoursesByIds(List<Integer> ids) {
        return courseClient.getCoursesByIds(ids)
                .stream()
                .collect(Collectors.toMap(CourseResponse::id, courseResponse -> courseResponse));
    }
}
