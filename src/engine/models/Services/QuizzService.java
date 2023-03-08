package engine.models.Services;

import engine.exception.NotOwnerException;
import engine.models.Entities.Answer;
import engine.models.Entities.DTO.QuizzDTO;
import engine.models.Entities.User;
import engine.repositories.AnswerRepository;
import engine.repositories.QuizzRepository;
import engine.models.Entities.Quizz;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class QuizzService {

    private final QuizzRepository quizzRepository;
    private final AnswerRepository answerRepository;
    private final ModelMapper modelMapper;


    @Autowired
    public QuizzService(QuizzRepository quizzRepository, AnswerRepository answerRepository, ModelMapper modelMapper) {
        this.quizzRepository = quizzRepository;
        this.answerRepository = answerRepository;
        this.modelMapper = modelMapper;
    }

    public Page<QuizzDTO> findAll(Pageable pageable) {
        return quizzRepository
                .findAll(pageable)
                .map(this::convertToDto);
    }


    public Quizz findById(int id) {
        Quizz quizz = quizzRepository.findById(id).orElse(null);
        if (quizz == null) {
            throw new IndexOutOfBoundsException();
        }
        return quizz;
    }

    @Transactional
    public void deleteById(int id, User user) {
        Quizz quizz = quizzRepository.findById(id).orElse(null);
        if (quizz == null) {
            throw new IndexOutOfBoundsException();
        } else if (!quizz.getUser().getEmail().equals(user.getEmail())) {
            throw new NotOwnerException();
        }
        quizzRepository.deleteById(id);
    }

    public Quizz save(Quizz toSave) {
        Quizz savedQuizz = this.quizzRepository.save(toSave);
        this.answerRepository.saveAll(savedQuizz.getOptionsObject());
        return savedQuizz;
    }


    public QuizzDTO convertToDto(Quizz quizz) {
        QuizzDTO quizzDTO = modelMapper.map(quizz, QuizzDTO.class);
        quizzDTO.setOptions(quizz.getOptions());
        quizzDTO.setAnswer(quizz.getAnswer());
        return quizzDTO;
    }

    public Quizz convertToEntity(QuizzDTO quizzDTO) {
        List<Answer> options = quizzDTO.getOptions()
                .stream()
                .map(x -> new Answer(x, false))
                .toList();
        List<Integer> answer = Optional
                .ofNullable(quizzDTO.getAnswer())
                .orElse(List.of());
        answer.forEach(x -> options.get(x).setCorrect(true));

        Quizz quizz = modelMapper.map(quizzDTO, Quizz.class);
        quizz.setOptions(options);
        return quizz;
    }



}
