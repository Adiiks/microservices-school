package pl.adrianpacholak.userservice.converter;

import org.springframework.stereotype.Component;
import pl.adrianpacholak.userservice.dto.StudentDTO;
import pl.adrianpacholak.userservice.dto.StudentResponse;
import pl.adrianpacholak.userservice.dto.UserDTO;
import pl.adrianpacholak.userservice.model.Student;

@Component
public class StudentConverter {

    public Student studentDtoToStudent(StudentDTO studentDTO) {
        UserDTO basicInfo = studentDTO.basicInfo();

        return Student.builder()
                .email(studentDTO.basicInfo().email())
                .names(basicInfo.names())
                .lastname(basicInfo.lastname())
                .pesel(basicInfo.pesel())
                .position(basicInfo.position())
                .facultyId(studentDTO.basicInfo().facultyId())
                .build();
    }

    public StudentResponse studentToStudentResponse(Student student) {
        String fullName = getFullName(student);
        return new StudentResponse(student.getId(), fullName);
    }

    private String getFullName(Student student) {
        return new StringBuilder(student.getNames())
                .append(" ")
                .append(student.getLastname())
                .toString();
    }
}
