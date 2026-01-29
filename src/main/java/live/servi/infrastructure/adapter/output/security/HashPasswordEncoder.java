package live.servi.infrastructure.adapter.output.security;
import live.servi.domain.port.output.PasswordEncoder;
import lombok.NoArgsConstructor;

import org.springframework.security.crypto.bcrypt.BCrypt;

@NoArgsConstructor
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
