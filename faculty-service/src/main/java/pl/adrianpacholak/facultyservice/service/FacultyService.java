package pl.adrianpacholak.facultyservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import pl.adrianpacholak.facultyservice.converter.FacultyConverter;
import pl.adrianpacholak.facultyservice.dto.FacultyRequest;
import pl.adrianpacholak.facultyservice.model.Faculty;
import pl.adrianpacholak.facultyservice.repository.FacultyRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FacultyService {

    private final FacultyRepository facultyRepository;
    private final FacultyConverter facultyConverter;

    @Transactional
    public void createFaculty(FacultyRequest request) {
        if (facultyRepository.existsByName(request.name())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "There already is faculty with that name");
        }

        Faculty newFaculty = facultyConverter.facultyRequestToFaculty(request);

        facultyRepository.save(newFaculty);
    }

    public boolean checkFacultyExists(Integer facultyId) {
        return facultyRepository.existsById(facultyId);
    }

    public Map<Integer, String> getFacultiesByIds(List<Integer> facultiesIds) {
        return facultyRepository.findByIdIn(facultiesIds)
                .stream()
                .collect(Collectors.toMap(Faculty::getId, Faculty::getName));
    }
}
