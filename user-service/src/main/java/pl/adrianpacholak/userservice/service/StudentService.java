package pl.adrianpacholak.userservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import pl.adrianpacholak.userservice.client.FacultyClient;
import pl.adrianpacholak.userservice.converter.StudentConverter;
import pl.adrianpacholak.userservice.dto.StudentDTO;
import pl.adrianpacholak.userservice.model.ERole;
import pl.adrianpacholak.userservice.model.Student;
import pl.adrianpacholak.userservice.repository.StudentRepository;
import pl.adrianpacholak.userservice.service.client.KeycloakClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentConverter studentConverter;
    private final KeycloakClient keycloakClient;
    private final FacultyClient facultyClient;

    @Transactional
    public void createStudent(StudentDTO studentDTO) {
        Map<String, Boolean> response = facultyClient.checkFacultyExists(studentDTO.basicInfo().facultyId());
        if (!response.get("exists")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Faculty with id: " + studentDTO.basicInfo().facultyId() + " not exists");
        }

        keycloakClient.createNewUser(studentDTO.basicInfo().pesel(), studentDTO.basicInfo().password(), ERole.STUDENT);

        Student newStudent = studentConverter.studentDtoToStudent(studentDTO);
        newStudent.setIsStillStudying(true);

        studentRepository.save(newStudent);
    }

    public Boolean checkStudentExists(String username) {
        return studentRepository.existsByPesel(username);
    }
}
