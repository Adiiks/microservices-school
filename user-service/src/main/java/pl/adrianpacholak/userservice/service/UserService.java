package pl.adrianpacholak.userservice.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.adrianpacholak.userservice.client.FacultyClient;
import pl.adrianpacholak.userservice.converter.UserConverter;
import pl.adrianpacholak.userservice.dto.FacultyDTO;
import pl.adrianpacholak.userservice.dto.UserResponse;
import pl.adrianpacholak.userservice.model.User;
import pl.adrianpacholak.userservice.repository.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final FacultyClient facultyClient;

    public Page<UserResponse> searchUsersByFullName(String fullName, Pageable pageable) {
        Page<User> userPage = userRepository.findByFullNameLike(fullName, pageable);

        if (userPage.getTotalElements() == 0) {
            return Page.empty();
        }

        Set<Integer> facultiesIds = userPage.get()
                .map(User::getFacultyId)
                .collect(Collectors.toSet());

        Map<Integer, String> faculties = facultyClient.getFacultiesByIds(facultiesIds);

        List<UserResponse> userResponseList = updateUsersWithFaculty(faculties, userPage.getContent());

        return new PageImpl<>(userResponseList, userPage.getPageable(), userPage.getTotalElements());
    }

    private List<UserResponse> updateUsersWithFaculty(Map<Integer, String> faculties, List<User> users) {
        return users.stream()
                .map(user -> userConverter.userToUserResponse(user,
                        new FacultyDTO(user.getFacultyId(), faculties.get(user.getFacultyId()))))
                .toList();
    }

    public Page<UserResponse> searchUsersByFacultyName(String facultyName, Pageable pageable) {
        Map<String, Integer> response = null;

        try {
            response = facultyClient.getFacultyId(facultyName);
        } catch (FeignException ex) {
            return Page.empty();
        }

        Page<User> users = userRepository.findByFacultyId(response.get("facultyId"), pageable);

        List<UserResponse> userResponseList = users.get()
                .map(user -> userConverter.userToUserResponse(user, new FacultyDTO(user.getFacultyId(), facultyName)))
                .toList();

        return new PageImpl<>(userResponseList, users.getPageable(), users.getTotalElements());
    }
}
