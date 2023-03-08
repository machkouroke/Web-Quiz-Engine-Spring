package engine.repositories;

import engine.models.Entities.Answer;

import engine.models.keys.AnswerKey;
import org.springframework.data.repository.CrudRepository;

public interface AnswerRepository extends CrudRepository<Answer, AnswerKey> {

}
