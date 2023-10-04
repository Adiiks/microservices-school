package pl.adrianpacholak.lessonservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;
import pl.adrianpacholak.lessonservice.client.CourseClient;
import pl.adrianpacholak.lessonservice.client.UserClient;
import pl.adrianpacholak.lessonservice.converter.LessonConverter;
import pl.adrianpacholak.lessonservice.converter.RegistrationConverter;
import pl.adrianpacholak.lessonservice.dto.CourseResponse;
import pl.adrianpacholak.lessonservice.dto.RegistrationRequest;
import pl.adrianpacholak.lessonservice.dto.RegistrationResponse;
import pl.adrianpacholak.lessonservice.dto.TeacherResponse;
import pl.adrianpacholak.lessonservice.model.*;
import pl.adrianpacholak.lessonservice.repository.LessonRepository;
import pl.adrianpacholak.lessonservice.repository.RegistrationRepository;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceTest {

    private RegistrationService registrationService;

    @Mock
    private RegistrationRepository registrationRepository;

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private UserClient userClient;

    @Mock
    private CourseClient courseClient;

    private RegistrationConverter registrationConverter = new RegistrationConverter(new LessonConverter());

    @Captor
    private ArgumentCaptor<Registration> registrationAc;

    @BeforeEach
    void setUp() {
        registrationService = new RegistrationService(registrationRepository, lessonRepository,
                registrationConverter, userClient, courseClient);
    }

    @DisplayName("Create new registration - lesson not found")
    @Test
    void openRegistration_LessonNotFound() {
        RegistrationRequest request = new RegistrationRequest("2023-10-03", "08:00:00", "2023-10-13", "23:59:59", 1);

        when(lessonRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> registrationService.openRegistration(request));
    }

    @DisplayName("Create new registration - invalid dates")
    @Test
    void openRegistration_InvalidDates() {
        RegistrationRequest request = new RegistrationRequest("2023-10-23", "08:00:00", "2023-10-13", "23:59:59", 1);

        when(lessonRepository.findById(anyInt())).thenReturn(Optional.of(buildLesson()));

        assertThrows(ResponseStatusException.class, () -> registrationService.openRegistration(request));
    }

    @DisplayName("Create new registration")
    @Test
    void openRegistration() {
        RegistrationRequest request = new RegistrationRequest("2023-10-03", "08:00:00", "2023-10-13", "23:59:59", 1);

        when(lessonRepository.findById(anyInt())).thenReturn(Optional.of(buildLesson()));

        registrationService.openRegistration(request);

        verify(registrationRepository).save(registrationAc.capture());
        Registration registration = registrationAc.getValue();

        assertNull(registration.getId());
        assertNotNull(registration.getBeginDateTime());
        assertNotNull(registration.getEndDateTime());
        assertNotNull(registration.getLesson());
    }

    @DisplayName("Get list of registrations")
    @Test
    void getRegistrations() {
        Registration registrationDb = buildRegistration();

        when(registrationRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(registrationDb), Pageable.unpaged(), 1));

        when(userClient.getTeachersByIds(anyList()))
                .thenReturn(List.of(new TeacherResponse(
                        registrationDb.getLesson().getTeacherId(), "Jan Kowalski")));

        when(courseClient.getCoursesByIds(anyList()))
                .thenReturn(List.of(new CourseResponse(registrationDb.getLesson().getCourseId(),
                        "Programming")));

        Page<RegistrationResponse> registrations =
                registrationService.getRegistrations("12345678901", Pageable.unpaged());

        assertRegistrationResponse(registrations.getContent().get(0), registrationDb);
        assertFalse(registrations.getContent().get(0).isStudentRegister());
    }

    private void assertRegistrationResponse(RegistrationResponse registrationResponse,
                                            Registration registration) {
        assertEquals(registration.getId(), registrationResponse.id());
        assertEquals(registration.getBeginDateTime(), registrationResponse.beginDateTime());
        assertEquals(registration.getEndDateTime(), registrationResponse.endDateTime());
        assertNotNull(registrationResponse.lesson());
    }

    private Lesson buildLesson() {
        return Lesson.builder()
                .id(1)
                .courseId(1)
                .term(ETerm.SUMMER)
                .status(EStatus.UPCOMING)
                .type(EType.LABORATORY)
                .day(DayOfWeek.FRIDAY)
                .beginTime("08:15")
                .endTime("09:45")
                .classroom(112)
                .totalStudentsSigned(0)
                .limitOfPlaces(30)
                .teacherId(1)
                .students(new HashSet<>())
                .build();
    }

    private Registration buildRegistration() {
        return Registration.builder()
                .id(1)
                .beginDateTime(LocalDateTime.now())
                .endDateTime(LocalDateTime.now().plusDays(10))
                .lesson(buildLesson())
                .build();
    }
}