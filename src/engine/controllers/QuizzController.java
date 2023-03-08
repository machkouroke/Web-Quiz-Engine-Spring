package engine.controllers;

import engine.exception.NotOwnerException;
import engine.models.Entities.DTO.QuizzDTO;
import engine.models.Entities.Quizz;
import engine.models.Entities.QuizzAnswering;
import engine.models.Entities.User;
import engine.models.Entities.UserDetailsImpl;
import engine.models.Services.QuizzService;
import engine.models.Services.UserService;
import engine.models.keys.QuizzAnsweringKey;
import engine.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class QuizzController {


    @Autowired
    QuizzService quizzService;

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;


    @PostMapping("/api/register")
    public ResponseEntity<Map<String, String>> register(@Valid @RequestBody User user) {
        try {
            userService.register(user);
            return ResponseEntity.ok(Map.of("email", user.getEmail()));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

    }

    @GetMapping("/api/quizzes")
    public Page<QuizzDTO> getQuizz(@RequestParam(required = false) Integer page) {
        Pageable pageable = PageRequest.of(Objects.isNull(page) ? 0 : page, 10);
        return quizzService.findAll(pageable);
    }

    @GetMapping("/api/quizzes/completed")
    public Page<QuizzAnsweringKey> getCompletedQuizzes(@AuthenticationPrincipal UserDetailsImpl details,
                                                       @RequestParam(required = false) Integer page) {
        User user = details.getUser(userRepository);
        Sort sortByCompletedAt = Sort.by("id.completedAt").descending();
        Pageable pageable = PageRequest.of(Objects.isNull(page) ? 0 : page, 10, sortByCompletedAt);
        return userService.getCompletedQuizzes(user, pageable).map(QuizzAnswering::getId);
    }

    @GetMapping("/api/quizzes/{id}")
    public ResponseEntity<QuizzDTO> getQuizz(@PathVariable int id) {
        try {
            Quizz quizz = quizzService.findById(id);
            return new ResponseEntity<>(quizzService.convertToDto(quizz), HttpStatus.OK);
        } catch (IndexOutOfBoundsException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Quizz not found");
        }
    }

    @DeleteMapping("/api/quizzes/{id}")
    public ResponseEntity<QuizzDTO> deleteQuizz(@AuthenticationPrincipal UserDetailsImpl details, @PathVariable int id) {
        User user = details.getUser(userRepository);
        try {
            quizzService.deleteById(id, user);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IndexOutOfBoundsException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Quizz not found");
        } catch (NotOwnerException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to delete this quizz");
        }
    }


    @PostMapping("/api/quizzes")
    public ResponseEntity<Quizz> createQuizz(@RequestBody @Valid QuizzDTO quizz,
                                             @AuthenticationPrincipal UserDetailsImpl details) {

        Quizz newQuizz = quizzService.convertToEntity(quizz);
        User user = details.getUser(userRepository);
        newQuizz.setUser(user);
        Quizz savedQuizz = quizzService.save(newQuizz);
        return new ResponseEntity<>(savedQuizz, HttpStatus.OK);
    }

    @PostMapping("/api/quizzes/{id}/solve")
    public ResponseEntity<Map<String, Object>> answerQuizz(
            @AuthenticationPrincipal UserDetailsImpl details,
            @PathVariable int id,
            @RequestBody Map<String, List<Integer>> answer) {
        try {
            User user = details.getUser(userRepository);
            Quizz selectedQuizz = quizzService.findById(id);
            boolean isCorrect = Objects.equals(selectedQuizz.getAnswer(), answer.get("answer"));
            String feedback = isCorrect ? "Congratulations, you're right!" : "Wrong answer! Please, try again.";
            Map<String, Object> response = Map.of("success", isCorrect, "feedback", feedback);
            if (isCorrect) {
                userService.completeQuizz(user, selectedQuizz);
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IndexOutOfBoundsException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Quizz not found");
        }

    }
}
