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
import pl.adrianpacholak.userservice.converter.StudentConverter;
import pl.adrianpacholak.userservice.dto.StudentDTO;
import pl.adrianpacholak.userservice.dto.UserDTO;
import pl.adrianpacholak.userservice.model.Student;
import pl.adrianpacholak.userservice.repository.StudentRepository;
import pl.adrianpacholak.userservice.service.client.KeycloakClient;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    StudentService studentService;

    @Mock
    StudentRepository studentRepository;

    @Mock
    KeycloakClient keycloakClient;

    @Mock
    FacultyClient facultyClient;

    StudentConverter studentConverter = new StudentConverter();

    @Captor
    ArgumentCaptor<Student> studentAc;

    @BeforeEach
    void setUp() {
        studentService = new StudentService(studentRepository, studentConverter, keycloakClient, facultyClient);
    }

    @DisplayName("Create new student - SUCCESS")
    @Test
    void createStudent() {
        UserDTO userDTO = new UserDTO("12345678901", "Jan", "Kowalski", "jan@gmail.com", "password", "lecturer", 11);
        StudentDTO studentDTO = new StudentDTO(userDTO);
        Map<String, Boolean> response = Collections.singletonMap("exists", true);

        when(facultyClient.checkFacultyExists(anyInt()))
                .thenReturn(response);

        studentService.createStudent(studentDTO);

        verify(keycloakClient).createNewUser(anyString(), anyString(), any());
        verify(studentRepository).save(studentAc.capture());

        Student studentDb = studentAc.getValue();

        assertEquals(studentDTO.basicInfo().pesel(), studentDb.getPesel());
        assertEquals(studentDTO.basicInfo().names(), studentDb.getNames());
        assertEquals(studentDTO.basicInfo().lastname(), studentDb.getLastname());
        assertEquals(studentDTO.basicInfo().email(), studentDb.getEmail());
        assertEquals(studentDTO.basicInfo().position(), studentDb.getPosition());
        assertEquals(studentDTO.basicInfo().facultyId(), studentDb.getFacultyId());
        assertNull(studentDb.getPageUrl());
        assertTrue(studentDb.getIsStillStudying());
    }

    @DisplayName("Create new student - Faculty Not Exists")
    @Test
    void createStudentFacultyNotExists() {
        UserDTO userDTO = new UserDTO("12345678901", "Jan", "Kowalski", "jan@gmail.com", "password", "lecturer", 11);
        StudentDTO studentDTO = new StudentDTO(userDTO);
        Map<String, Boolean> response = Collections.singletonMap("exists", false);

        when(facultyClient.checkFacultyExists(anyInt()))
                .thenReturn(response);

        assertThrows(ResponseStatusException.class, () -> studentService.createStudent(studentDTO));
    }
}