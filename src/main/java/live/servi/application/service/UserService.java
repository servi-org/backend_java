package live.servi.application.service;

import org.springframework.stereotype.Service;

import live.servi.application.port.input.CreateUserUseCase;
import live.servi.domain.exception.DomainException;
import live.servi.domain.model.User;
import live.servi.domain.port.output.UserRepository;

/**
 * Servicio de app - Implementa los casos de uso
 * implementa la logica de negocio usando los puertos (input)
 */
@Service
public class UserService implements CreateUserUseCase {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User user) {
        // validar el usuario
        try {
            user.validate();
        } catch (IllegalArgumentException e) {
            throw DomainException.badRequest("BAD_REQUEST", e.getMessage());
        }

        // verificar q no exista
        userRepository.findByEmail(user.getEmail())
                .ifPresent(existingUser -> {
                    throw DomainException.conflict(
                        "USER_ALREADY_EXISTS", 
                        "El usuario con email " + user.getEmail() + " ya existe"
                    );
                });

        // guardar y retornar
        return userRepository.save(user);
    }
}
