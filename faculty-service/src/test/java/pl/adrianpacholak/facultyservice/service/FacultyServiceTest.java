package pl.adrianpacholak.facultyservice.service;

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
import pl.adrianpacholak.facultyservice.converter.FacultyConverter;
import pl.adrianpacholak.facultyservice.dto.FacultyRequest;
import pl.adrianpacholak.facultyservice.dto.FacultyResponse;
import pl.adrianpacholak.facultyservice.model.Faculty;
import pl.adrianpacholak.facultyservice.repository.FacultyRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    @DisplayName("Get list of faculties based on list of ids")
    @Test
    void getFacultiesByIds() {
        List<Integer> facultiesIds = List.of(1);
        Faculty facultyDb = Faculty.builder()
                .id(1)
                .name("Faculty of Computer Science")
                .build();
        List<Faculty> facultiesDb = List.of(facultyDb);

        when(facultyRepository.findByIdIn(facultiesIds))
                .thenReturn(facultiesDb);

        Map<Integer, String> faculties = facultyService.getFacultiesByIds(facultiesIds);
        assertEquals(facultyDb.getName(), faculties.get(facultyDb.getId()));
    }

    @DisplayName("Get faculty ID based on name - Not found")
    @Test
    void getFacultyIdByName_NotFound() {
        when(facultyRepository.findByName(anyString())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
                () -> facultyService.getFacultyIdByName("Faculty of Computer Science"));
    }

    @DisplayName("Get faculty ID based on name")
    @Test
    void getFacultyIdByName() {
        Integer facultyId = 1;
        Faculty faculty = Faculty.builder()
                .id(facultyId)
                .build();

        when(facultyRepository.findByName(anyString())).thenReturn(Optional.of(faculty));

        Map<String, Integer> facultyIdResponse = facultyService.getFacultyIdByName("Faculty of Computer Science");

        assertEquals(facultyId, facultyIdResponse.get("facultyId"));
    }

    @DisplayName("Get list of all faculties")
    @Test
    void getFaculties() {
        Page<Faculty> facultyPage = new PageImpl<>(List.of(buildFaculty()), Pageable.unpaged(), 1);

        when(facultyRepository.findAll(any(Pageable.class))).thenReturn(facultyPage);

        Page<FacultyResponse> facultyResponsePage = facultyService.getFaculties(Pageable.unpaged());

        assertEquals(1, facultyResponsePage.getTotalElements());
    }

    private Faculty buildFaculty() {
        return Faculty.builder()
                .id(1)
                .name("Faculty of Computer Science")
                .phoneNumber("123456789")
                .address("ul.Mikołajki 12")
                .build();
    }
}