package live.servi.infrastructure.adapter.input.rest.mapper;

import org.springframework.stereotype.Component;

import live.servi.domain.model.User;
import live.servi.infrastructure.adapter.input.rest.dto.CreateUserRequest;
import live.servi.infrastructure.adapter.input.rest.dto.UserResponse;

/**
 * Mapper para convertir entre DTOs y modelo de dominio
 */
@Component
public class UserRestMapper {

    /**
     * Convierte el request a modelo de dominio
     */
    public User toDomain(CreateUserRequest request) {
        return User.builder()
                .name(request.getName())
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
