package engine.repositories;


import engine.models.Entities.Quizz;
import engine.models.Entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface QuizzRepository extends PagingAndSortingRepository<Quizz, Integer>,  CrudRepository<Quizz, Integer> {
    Optional<Quizz> findByIdAndUser(@Param("id") int id, @Param("user") User user);

}