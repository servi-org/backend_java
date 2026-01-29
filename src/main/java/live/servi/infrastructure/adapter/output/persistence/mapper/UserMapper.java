package live.servi.infrastructure.adapter.output.persistence.mapper;

import org.springframework.stereotype.Component;

import live.servi.domain.model.User;
import live.servi.infrastructure.adapter.output.persistence.entity.UserEntity;

/**
 * Convierte entre el modelo de dominio y la entidad JPA
 */
@Component
public class UserMapper {

    /**
     * Convierte de entidad JPA a modelo de dominio
     */
    public User toDomain(UserEntity entity) {
        return User.builder()
                .id(entity.getId())
                .name(entity.getName())
                .email(entity.getEmail())
                .build();
    }

    /**
     * Convierte de modelo de dominio a entidad JPA
     */
    public UserEntity toEntity(User user) {
        return UserEntity.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
