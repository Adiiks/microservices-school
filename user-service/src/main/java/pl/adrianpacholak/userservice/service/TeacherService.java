package pl.adrianpacholak.userservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.adrianpacholak.userservice.converter.TeacherConverter;
import pl.adrianpacholak.userservice.dto.TeacherDTO;
import pl.adrianpacholak.userservice.model.ERole;
import pl.adrianpacholak.userservice.model.Teacher;
import pl.adrianpacholak.userservice.repository.TeacherRepository;
import pl.adrianpacholak.userservice.service.client.KeycloakClient;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final TeacherConverter teacherConverter;
    private final KeycloakClient keycloakClient;

    @Transactional
    public void createTeacher(TeacherDTO teacherDTO) {
        keycloakClient.createNewUser(teacherDTO.basicInfo().pesel(), teacherDTO.basicInfo().password(), ERole.TEACHER);

        Teacher newTeacher = teacherConverter.teacherDtoToTeacher(teacherDTO);

        teacherRepository.save(newTeacher);
    }
}
