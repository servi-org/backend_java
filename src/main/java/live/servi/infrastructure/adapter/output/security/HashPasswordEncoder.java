package live.servi.infrastructure.adapter.output.security;
import org.springframework.security.crypto.bcrypt.BCrypt;

import live.servi.domain.port.output.security.PasswordEncoder;

public class HashPasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(String rawPassword) {
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt());
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return BCrypt.checkpw(rawPassword, encodedPassword);
    }    
}
