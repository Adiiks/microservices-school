package pl.adrianpacholak.userservice.converter;

import org.springframework.stereotype.Component;
import pl.adrianpacholak.userservice.dto.TeacherDTO;
import pl.adrianpacholak.userservice.dto.TeacherResponse;
import pl.adrianpacholak.userservice.dto.UserDTO;
import pl.adrianpacholak.userservice.model.Teacher;

@Component
public class TeacherConverter {

    public Teacher teacherDtoToTeacher(TeacherDTO teacherDTO) {
        UserDTO basicInfo = teacherDTO.basicInfo();

        return Teacher.builder()
                .email(teacherDTO.basicInfo().email())
                .names(basicInfo.names())
                .lastname(basicInfo.lastname())
                .pesel(basicInfo.pesel())
                .phoneNumber(teacherDTO.phoneNumber())
                .officeNumber(teacherDTO.officeNumber())
                .position(basicInfo.position())
                .facultyId(teacherDTO.basicInfo().facultyId())
                .build();
    }

    public TeacherResponse teacherToTeacherResponse(Teacher teacher) {
        return new TeacherResponse(teacher.getId(), buildFullName(teacher));
    }

    private String buildFullName(Teacher teacher) {
        return new StringBuilder(teacher.getNames())
                .append(" ")
                .append(teacher.getLastname())
                .toString();
    }
}
