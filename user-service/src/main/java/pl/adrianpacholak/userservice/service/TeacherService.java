package pl.adrianpacholak.userservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import pl.adrianpacholak.userservice.client.FacultyClient;
import pl.adrianpacholak.userservice.converter.TeacherConverter;
import pl.adrianpacholak.userservice.dto.TeacherDTO;
import pl.adrianpacholak.userservice.model.ERole;
import pl.adrianpacholak.userservice.model.Teacher;
import pl.adrianpacholak.userservice.repository.TeacherRepository;
import pl.adrianpacholak.userservice.service.client.KeycloakClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final TeacherConverter teacherConverter;
    private final KeycloakClient keycloakClient;
    private final FacultyClient facultyClient;

    @Transactional
    public void createTeacher(TeacherDTO teacherDTO) {
        Map<String, Boolean> response = facultyClient.checkFacultyExists(teacherDTO.basicInfo().facultyId());
        if (!response.get("exists")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Faculty with id: " + teacherDTO.basicInfo().facultyId() + " not exists");
        }

        keycloakClient.createNewUser(teacherDTO.basicInfo().pesel(), teacherDTO.basicInfo().password(), ERole.TEACHER);

        Teacher newTeacher = teacherConverter.teacherDtoToTeacher(teacherDTO);

        teacherRepository.save(newTeacher);
    }

    public boolean checkTeacherExists(Integer teacherId) {
        return teacherRepository.existsById(teacherId);
    }
}
