package ivan.prh.app.repository;

import ivan.prh.app.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends CrudRepository<User, Long> {
    Optional<User> findUserById(Long id);
    Optional<User> findUserByUserName(String userName);
    List<User> findAll();
    List<User> findAll(Pageable pageable);
}
