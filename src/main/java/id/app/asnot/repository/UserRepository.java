package id.app.asnot.repository;

import id.app.asnot.model.entity.Role;
import id.app.asnot.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    List<User> findAllByRole(Role role);
}
