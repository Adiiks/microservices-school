package pl.adrianpacholak.userservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import pl.adrianpacholak.userservice.client.FacultyClient;
import pl.adrianpacholak.userservice.converter.TeacherConverter;
import pl.adrianpacholak.userservice.dto.TeacherDTO;
import pl.adrianpacholak.userservice.dto.TeacherResponse;
import pl.adrianpacholak.userservice.dto.UserDTO;
import pl.adrianpacholak.userservice.model.Teacher;
import pl.adrianpacholak.userservice.repository.TeacherRepository;
import pl.adrianpacholak.userservice.service.client.KeycloakClient;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {

    TeacherService teacherService;

    @Mock
    TeacherRepository teacherRepository;

    @Mock
    KeycloakClient keycloakClient;

    @Mock
    FacultyClient facultyClient;

    TeacherConverter teacherConverter = new TeacherConverter();

    @Captor
    ArgumentCaptor<Teacher> teacherAc;

    @BeforeEach
    void setUp() {
        teacherService = new TeacherService(teacherRepository, teacherConverter, keycloakClient, facultyClient);
    }

    @DisplayName("Create new teacher - SUCCESS")
    @Test
    void createTeacher() {
        UserDTO userDTO = new UserDTO("12345678901", "Jan", "Kowalski", "jan@gmail.com", "password", "lecturer", 12);
        TeacherDTO teacherDTO = new TeacherDTO(userDTO, "123456789", 10);

        Map<String, Boolean> response = Collections.singletonMap("exists", true);

        when(facultyClient.checkFacultyExists(anyInt()))
                .thenReturn(response);

        teacherService.createTeacher(teacherDTO);

        verify(keycloakClient).createNewUser(anyString(), anyString(), any());
        verify(teacherRepository).save(teacherAc.capture());

        Teacher teacherDb = teacherAc.getValue();

        assertEquals(teacherDTO.basicInfo().pesel(), teacherDb.getPesel());
        assertEquals(teacherDTO.basicInfo().names(), teacherDb.getNames());
        assertEquals(teacherDTO.basicInfo().lastname(), teacherDb.getLastname());
        assertEquals(teacherDTO.basicInfo().email(), teacherDb.getEmail());
        assertEquals(teacherDTO.phoneNumber(), teacherDb.getPhoneNumber());
        assertEquals(teacherDTO.officeNumber(), teacherDb.getOfficeNumber());
        assertEquals(teacherDTO.basicInfo().position(), teacherDb.getPosition());
        assertNull(teacherDb.getPageUrl());
    }

    @DisplayName("Create new teacher - Faculty Not Exists")
    @Test
    void createTeacherFacultyNotExists() {
        UserDTO userDTO = new UserDTO("12345678901", "Jan", "Kowalski", "jan@gmail.com", "password", "lecturer", 12);
        TeacherDTO teacherDTO = new TeacherDTO(userDTO, "123456789", 10);

        Map<String, Boolean> response = Collections.singletonMap("exists", false);

        when(facultyClient.checkFacultyExists(anyInt()))
                .thenReturn(response);

        assertThrows(ResponseStatusException.class, () -> teacherService.createTeacher(teacherDTO));
    }

    @DisplayName("Get list of teachers based on list of IDs")
    @Test
    void getTeachersByIds() {
        List<Integer> ids = List.of(1);
        Teacher teacherDb = buildTeacher();

        when(teacherRepository.findAllByIdIn(anyList())).thenReturn(List.of(teacherDb));

        List<TeacherResponse> teachers = teacherService.getTeachersByIds(ids);
        TeacherResponse teacher = teachers.get(0);

        assertNotNull(teacher.id());
        assertNotNull(teacher.fullName());
    }

    private Teacher buildTeacher() {
        return Teacher.builder()
                .id(1)
                .names("Jan")
                .lastname("Kowalski")
                .build();
    }
}