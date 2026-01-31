package live.servi.domain.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Credential {
    private String email;
    private String password;
}