package pl.adrianpacholak.facultyservice.converter;

import org.springframework.stereotype.Component;
import pl.adrianpacholak.facultyservice.dto.FacultyRequest;
import pl.adrianpacholak.facultyservice.dto.FacultyResponse;
import pl.adrianpacholak.facultyservice.model.Faculty;

@Component
public class FacultyConverter {

    public Faculty facultyRequestToFaculty(FacultyRequest request) {
        return Faculty.builder()
                .name(request.name())
                .address(request.address())
                .phoneNumber(request.phoneNumber())
                .build();
    }

    public FacultyResponse facultyToFacultyResponse(Faculty faculty) {
        return new FacultyResponse(faculty.getId(), faculty.getName(), faculty.getAddress(), faculty.getPhoneNumber());
    }
}
