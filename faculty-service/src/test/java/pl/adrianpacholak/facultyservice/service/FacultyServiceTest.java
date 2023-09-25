package pl.adrianpacholak.facultyservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import pl.adrianpacholak.facultyservice.converter.FacultyConverter;
import pl.adrianpacholak.facultyservice.dto.FacultyRequest;
import pl.adrianpacholak.facultyservice.model.Faculty;
import pl.adrianpacholak.facultyservice.repository.FacultyRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FacultyServiceTest {

    private FacultyService facultyService;

    @Mock
    private FacultyRepository facultyRepository;

    private FacultyConverter facultyConverter = new FacultyConverter();

    @Captor
    ArgumentCaptor<Faculty> facultyAc;

    @BeforeEach
    void setUp() {
        facultyService = new FacultyService(facultyRepository, facultyConverter);
    }

    @DisplayName("Create new faculty - Faculty name already in use")
    @Test
    void createFacultyNameNotAvailable() {
        FacultyRequest request = new FacultyRequest("Computer Science", "ul.Mikolajki 21, Poznań", "123456789");

        when(facultyRepository.existsByName(request.name()))
                .thenReturn(true);

        assertThrows(ResponseStatusException.class, () -> facultyService.createFaculty(request));

        verify(facultyRepository, times(0)).save(any());
    }

    @DisplayName("Create new faculty - SUCCESS")
    @Test
    void createFaculty() {
        FacultyRequest request = new FacultyRequest("Computer Science", "ul.Mikolajki 21, Poznań", "123456789");

        when(facultyRepository.existsByName(request.name()))
                .thenReturn(false);

        facultyService.createFaculty(request);

        verify(facultyRepository).save(facultyAc.capture());

        Faculty faculty = facultyAc.getValue();
        assertNull(faculty.getId());
        assertEquals(request.name(), faculty.getName());
        assertEquals(request.phoneNumber(), faculty.getPhoneNumber());
        assertEquals(request.address(), faculty.getAddress());
    }
}