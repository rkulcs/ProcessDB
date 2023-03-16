package processdb.backend.users;

import org.springframework.data.repository.Repository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface UserRepository extends Repository<User, Long> {

    User save(User user);

    User findByUsername(String username);
}
