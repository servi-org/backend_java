package live.servi.domain.port.output;

import java.util.UUID;

/**
 * Define la interfaz a usar para generar el JWT
 */
public interface TokenGenerator {
    String generateToken(UUID userId, String email);
}
