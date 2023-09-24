package pl.adrianpacholak.userservice.converter;

import org.springframework.stereotype.Component;
import pl.adrianpacholak.userservice.dto.StudentDTO;
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
                .build();
    }
}
