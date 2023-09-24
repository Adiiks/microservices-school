package pl.adrianpacholak.userservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.adrianpacholak.userservice.converter.TeacherConverter;
import pl.adrianpacholak.userservice.dto.TeacherDTO;
import pl.adrianpacholak.userservice.dto.UserDTO;
import pl.adrianpacholak.userservice.model.Teacher;
import pl.adrianpacholak.userservice.repository.TeacherRepository;
import pl.adrianpacholak.userservice.service.client.KeycloakClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {

    TeacherService teacherService;

    @Mock
    TeacherRepository teacherRepository;

    @Mock
    KeycloakClient keycloakClient;

    TeacherConverter teacherConverter = new TeacherConverter();

    @Captor
    ArgumentCaptor<Teacher> teacherAc;

    @BeforeEach
    void setUp() {
        teacherService = new TeacherService(teacherRepository, teacherConverter, keycloakClient);
    }

    @DisplayName("Create new teacher - SUCCESS")
    @Test
    void createTeacher() {
        UserDTO userDTO = new UserDTO("12345678901", "Jan", "Kowalski", "jan@gmail.com", "password");
        TeacherDTO teacherDTO = new TeacherDTO(userDTO, "123456789", 10);

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
        assertNull(teacherDb.getPageUrl());
    }
}