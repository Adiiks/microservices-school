package pl.adrianpacholak.userservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.adrianpacholak.userservice.converter.StudentConverter;
import pl.adrianpacholak.userservice.dto.StudentDTO;
import pl.adrianpacholak.userservice.model.ERole;
import pl.adrianpacholak.userservice.model.Student;
import pl.adrianpacholak.userservice.repository.StudentRepository;
import pl.adrianpacholak.userservice.service.client.KeycloakClient;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentConverter studentConverter;
    private final KeycloakClient keycloakClient;

    @Transactional
    public void createStudent(StudentDTO studentDTO) {
        keycloakClient.createNewUser(studentDTO.basicInfo().pesel(), studentDTO.basicInfo().password(), ERole.STUDENT);

        Student newStudent = studentConverter.studentDtoToStudent(studentDTO);
        newStudent.setIsStillStudying(true);

        studentRepository.save(newStudent);
    }
}
