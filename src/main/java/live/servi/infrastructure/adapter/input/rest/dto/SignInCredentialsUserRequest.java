package live.servi.infrastructure.adapter.input.rest.dto;

import lombok.Getter;

@Getter
public class SignInCredentialsUserRequest {
    String email;
    String password;
}