package live.servi.infrastructure.adapter.input.rest.mapper;

import org.springframework.stereotype.Component;

import live.servi.domain.model.Credential;
import live.servi.domain.model.User;
import live.servi.infrastructure.adapter.input.rest.dto.SignUpCredentialUserRequest;
import live.servi.infrastructure.adapter.input.rest.dto.SignInCredentialsUserRequest;
import live.servi.infrastructure.adapter.input.rest.dto.UserResponse;

/**
 * Mapper para convertir entre DTOs y modelo de dominio
 */
@Component
public class UserRestMapper {

    /**
     * Convierte el request del create User a modelo de dominio
     */
    public User toDomain(SignUpCredentialUserRequest request) {
        return User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword())
                .build();
    }

    /**
     * Convierte el request del sign in User con credenciales a modelo de dominio
     */
    public Credential toDomain(SignInCredentialsUserRequest request) {
        return Credential.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .build();
    }

    /**
     * Convierte el modelo de dominio a response
     */
    public UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
