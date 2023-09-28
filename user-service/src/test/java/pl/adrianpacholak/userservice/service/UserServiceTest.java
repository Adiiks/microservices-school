package pl.adrianpacholak.userservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import pl.adrianpacholak.userservice.client.FacultyClient;
import pl.adrianpacholak.userservice.converter.UserConverter;
import pl.adrianpacholak.userservice.dto.UserResponse;
import pl.adrianpacholak.userservice.model.User;
import pl.adrianpacholak.userservice.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private UserService userService;

    @Mock
    private UserRepository userRepository;

    private UserConverter userConverter = new UserConverter();

    @Mock
    private FacultyClient facultyClient;

    @Captor
    private ArgumentCaptor<Set<Integer>> facultiesIdsAc;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, userConverter, facultyClient);
    }

    @DisplayName("Get empty page of users based on full name")
    @Test
    void searchUsersByFullName_EmptyPage() {
        when(userRepository.findByFullNameLike(anyString(), any()))
                .thenReturn(Page.empty());

        Page<UserResponse> userPage = userService.searchUsersByFullName("Jan Kowalski", Pageable.unpaged());

        assertEquals(0, userPage.getTotalElements());
    }

    @DisplayName("Get page of users based on full name")
    @Test
    void searchUsersByFullName() {
        User user = buildUser();
        Page<User> userPage = new PageImpl<>(List.of(user), Pageable.unpaged(), 1);
        Map<Integer, String> faculties = Collections.singletonMap(user.getFacultyId(),
                "Faculty of Computer Science");

        when(userRepository.findByFullNameLike(anyString(), any()))
                .thenReturn(userPage);

        when(facultyClient.getFacultiesByIds(anySet()))
                .thenReturn(faculties);

        Page<UserResponse> userResponsePage = userService.searchUsersByFullName("Jan Kowalski",
                Pageable.unpaged());

        verify(facultyClient).getFacultiesByIds(facultiesIdsAc.capture());

        assertEquals(1, facultiesIdsAc.getValue().size());
        assertEquals(1, userResponsePage.getContent().size());
    }

    private User buildUser() {
        return User.builder()
                .names("Jan")
                .id(1)
                .email("jan@gmail.com")
                .facultyId(1)
                .lastname("Kowalski")
                .pageUrl("www.home.pl")
                .position("teacher")
                .pesel("12345678901")
                .build();
    }
}