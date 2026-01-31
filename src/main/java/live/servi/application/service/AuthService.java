package live.servi.application.service;

import org.springframework.stereotype.Service;

import live.servi.application.port.input.AuthUseCase;
import live.servi.domain.exception.DomainException;
import live.servi.domain.model.Credential;
import live.servi.domain.model.User;
import live.servi.domain.port.output.PasswordEncoder;
import live.servi.domain.port.output.UserRepository;

/**
 * Servicio de app - Implementa los casos de uso
 * implementa la logica de negocio usando los puertos (input)
 */
@Service
public class AuthService implements AuthUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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

        // cifrar la contraseña antes de guardar
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // guardar y retornar
        return userRepository.save(user);
    }

    public User signInCredentials(Credential credential) {
        User user = userRepository.findByEmail(credential.getEmail())
                .orElseThrow(() -> DomainException.unauthorized(
                    "UNAUTHORIZED", 
                    "Credenciales inválidas"
                ));

        if (!passwordEncoder.matches(credential.getPassword(), user.getPassword())) {
            throw DomainException.unauthorized(
                "UNAUTHORIZED", 
                "Credenciales inválidas"
            );
        }

        return user;
    }
}
