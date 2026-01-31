package live.servi.infrastructure.adapter.input.rest;

import jakarta.validation.Valid;

import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import live.servi.application.port.input.AuthUseCase;
import live.servi.domain.exception.DomainException;
import live.servi.domain.model.Credential;
import live.servi.domain.model.TokenParse;
import live.servi.domain.model.User;
import live.servi.domain.port.output.TokenGenerator;
import live.servi.infrastructure.adapter.input.rest.dto.SignInCredentialsUserRequest;
import live.servi.infrastructure.adapter.input.rest.dto.SignUpCredentialUserRequest;
import live.servi.infrastructure.adapter.input.rest.dto.UserResponse;
import live.servi.infrastructure.adapter.input.rest.mapper.UserRestMapper;


/**
 * Controlador REST - Adaptador de entrada
 * Expone los endpoints
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthUseCase createUserUseCase;
    private final UserRestMapper userRestMapper;
    private final TokenGenerator tokenGenerator;

    public AuthController(AuthUseCase createUserUseCase, UserRestMapper userRestMapper, TokenGenerator tokenGenerator) {
        this.createUserUseCase = createUserUseCase;
        this.userRestMapper = userRestMapper;
        this.tokenGenerator = tokenGenerator;
    }

    /**
     * Endpoint POST /users para crear un usuario
     * 
     * @param request Los datos del usuario a crear (se validan con @Valid)
     * @return El usuario creado con status 201 CREATED
     */
    @PostMapping("/signup-credentials")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody SignUpCredentialUserRequest request) {
        //convertir el DTO a modelo de dominio
        User user = userRestMapper.toDomain(request);

        //ejecutar el caso de uso
        User createdUser = createUserUseCase.createUser(user);

        //convertir el resultado a DTO de respuesta
        UserResponse response = userRestMapper.toResponse(createdUser);

        //generar el token JWT
        String token = tokenGenerator.generateToken(createdUser.getId(), createdUser.getEmail());
        
        //agregar el token en el header Authorization
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        
        //retornar con status 201 CREATED
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .headers(headers)
                .body(response);
    }

    @PostMapping("/signin-credentials")
    public ResponseEntity<UserResponse> signInCredentials(@RequestBody SignInCredentialsUserRequest request) {
        //convertir el DTO a modelo de dominio
        Credential credential = userRestMapper.toDomain(request);

        //ejecutar el caso de uso
        User user = createUserUseCase.signInCredentials(credential);   
        
        //generar el token JWT
        String token = tokenGenerator.generateToken(user.getId(), user.getEmail());
        
        //agregar el token en el header Authorization
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        //convertir el resultado a DTO de respuesta
        UserResponse response = userRestMapper.toResponse(user);
        
        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(headers)
                .body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> me(@RequestHeader(value = "Authorization",  required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw DomainException.unauthorized(
                "UNAUTHORIZED", 
                "Token invalido"
            );
        }

        // Remover el prefijo "Bearer " del token
        String token = authHeader.replace("Bearer ", "");

        // Obtener la información del usuario a partir del token
        TokenParse tokenParsed = tokenGenerator.parseToken(token);

        // Llamar al caso de uso para obtener los detalles del usuario
        User user = createUserUseCase.getSession(tokenParsed);
        
        // Convertir el resultado a DTO de respuesta
        UserResponse response = userRestMapper.toResponse(user);
        
        return ResponseEntity.ok(response);
    }
    
    
}
