package pl.adrianpacholak.userservice.converter;

import org.springframework.stereotype.Component;
import pl.adrianpacholak.userservice.dto.FacultyDTO;
import pl.adrianpacholak.userservice.dto.UserResponse;
import pl.adrianpacholak.userservice.model.User;

@Component
public class UserConverter {

    public UserResponse userToUserResponse(User user, FacultyDTO facultyDTO) {
        return new UserResponse(user.getId(), user.getNames(), user.getLastname(), user.getEmail(), user.getPosition(),
                user.getPageUrl(), facultyDTO);
    }
}
