package engine.models.Services;

import engine.models.Entities.DTO.QuizzDTO;
import engine.models.Entities.Quizz;
import engine.models.Entities.QuizzAnswering;
import engine.models.Entities.User;
import engine.models.keys.QuizzAnsweringKey;
import engine.repositories.AnswerRepository;
import engine.repositories.QuizzAnsweringRepository;
import engine.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.List;

@Service
public class UserService {
    public final UserRepository userRepository;

    public final QuizzAnsweringRepository quizzAnsweringRepository;
    public final PasswordEncoder encoder;

    @Autowired
    public UserService(UserRepository userRepository, QuizzAnsweringRepository quizzAnsweringRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.quizzAnsweringRepository = quizzAnsweringRepository;
        this.encoder = encoder;
    }

    public boolean existsByEmail(User user) {
        return userRepository.findById(user.getEmail()).isPresent();
    }

    public void register(User user) throws IllegalArgumentException {
        if (existsByEmail(user)) {
            throw new IllegalArgumentException("User with this email already exists");
        }
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        userRepository.save(user);
    }


    public void completeQuizz(User user, Quizz quizz) {
        QuizzAnswering quizzAnswering = new QuizzAnswering(user, quizz);
        quizzAnswering.setId(new QuizzAnsweringKey(quizz.getId(), user.getEmail(), new Date()));
        quizzAnsweringRepository.save(quizzAnswering);
    }

    public Page<QuizzAnswering> getCompletedQuizzes(User user, Pageable pageable) {
        return quizzAnsweringRepository.findByUser(user, pageable);
    }
}
