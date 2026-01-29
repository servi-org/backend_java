package live.servi.domain.port.output;

import java.util.Optional;
import java.util.UUID;

import live.servi.domain.model.User;

/**
 * Puerto de salida - Define la interfaz para persistencia
 */
public interface UserRepository {
    User save(User user);
    Optional<User> findByEmail(String email);
    Optional<User> findById(UUID id);
}
