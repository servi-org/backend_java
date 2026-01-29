package live.servi.infrastructure.adapter.output.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import live.servi.infrastructure.adapter.output.persistence.entity.UserEntity;

import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio JPA - interfaces con Spring Data JPA
 */
@Repository
public interface JpaUserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByEmail(String email);
}
