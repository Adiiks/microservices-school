package pl.adrianpacholak.lessonservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import pl.adrianpacholak.lessonservice.converter.RegistrationConverter;
import pl.adrianpacholak.lessonservice.dto.RegistrationRequest;
import pl.adrianpacholak.lessonservice.model.Lesson;
import pl.adrianpacholak.lessonservice.model.Registration;
import pl.adrianpacholak.lessonservice.repository.LessonRepository;
import pl.adrianpacholak.lessonservice.repository.RegistrationRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceTest {

    private RegistrationService registrationService;

    @Mock
    private RegistrationRepository registrationRepository;

    @Mock
    private LessonRepository lessonRepository;

    private RegistrationConverter registrationConverter = new RegistrationConverter();

    @Captor
    private ArgumentCaptor<Registration> registrationAc;

    @BeforeEach
    void setUp() {
        registrationService = new RegistrationService(registrationRepository, lessonRepository,
                registrationConverter);
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

    private Lesson buildLesson() {
        return Lesson.builder()
                .id(1)
                .build();
    }
}