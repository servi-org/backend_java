package live.servi.domain.model;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Entidad de dominio User
 * es la representacion pura del negocio
 */
@Getter
@Setter
@Builder
public class User {
    private UUID id;
    private String name;
    private String email;
    private String password;

    /**
     * Valida que el usuario tenga datos validos
     * este metodo se llama despues de contruir el obj
     */
    public void validate() {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("El email no es válido");
        }
    }
}
