package engine.repositories;

import engine.models.Entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

public  interface UserRepository extends CrudRepository<User, String> {
}
