package engine.repositories;


import engine.models.Entities.QuizzAnswering;
import engine.models.Entities.User;
import engine.models.keys.AnswerKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;


import java.util.List;

public interface QuizzAnsweringRepository extends PagingAndSortingRepository<QuizzAnswering, AnswerKey>,
        CrudRepository<QuizzAnswering, AnswerKey> {

    Page<QuizzAnswering> findByUser(User user, Pageable pageable);
}
