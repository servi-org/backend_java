package live.servi.infrastructure.adapter.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import live.servi.application.port.input.AuthUseCase;
import live.servi.domain.exception.DomainException;
import live.servi.domain.model.User;
import live.servi.infrastructure.adapter.input.rest.AuthController;
import live.servi.infrastructure.adapter.input.rest.dto.CreateUserRequest;
import live.servi.infrastructure.adapter.input.rest.dto.UserResponse;
import live.servi.infrastructure.adapter.input.rest.mapper.UserRestMapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.UUID;

/**
 * Test de integración para el UserController
 * Prueba la capa REST sin iniciar toda la aplicación
 */
@WebMvcTest(AuthController.class)
@DisplayName("UserController - Tests de Integración")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthUseCase createUserUseCase;

    @MockBean
    private UserRestMapper userRestMapper;

    @Test
    @DisplayName("POST /users debe crear un usuario y retornar 201")
    void shouldCreateUserAndReturn201() throws Exception {
        // Given
        CreateUserRequest request = CreateUserRequest.builder()
                .name("Juan Pérez")
                .email("juan@example.com")
                .build();

        User domainUser = User.builder()
                .name("Juan Pérez")
                .email("juan@example.com")
                .build();

        UUID userId = UUID.randomUUID();
        User createdUser = User.builder()
                .id(userId)
                .name("Juan Pérez")
                .email("juan@example.com")
                .build();

        UserResponse response = UserResponse.builder()
                .id(userId)
                .name("Juan Pérez")
                .email("juan@example.com")
                .build();

        when(userRestMapper.toDomain(any())).thenReturn(domainUser);
        when(createUserUseCase.createUser(any())).thenReturn(createdUser);
        when(userRestMapper.toResponse(any())).thenReturn(response);

        // When & Then
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(userId.toString()))
                .andExpect(jsonPath("$.name").value("Juan Pérez"))
                .andExpect(jsonPath("$.email").value("juan@example.com"));
    }

    @Test
    @DisplayName("POST /users debe retornar 400 cuando el nombre está vacío")
    void shouldReturn400WhenNameIsEmpty() throws Exception {
        // Given
        CreateUserRequest request = CreateUserRequest.builder()
                .name("")
                .email("juan@example.com")
                .build();

        // When & Then
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"));
    }

    @Test
    @DisplayName("POST /users debe retornar 400 cuando el email es inválido")
    void shouldReturn400WhenEmailIsInvalid() throws Exception {
        // Given
        CreateUserRequest request = CreateUserRequest.builder()
                .name("Juan Pérez")
                .email("invalid-email")
                .build();

        // When & Then
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"));
    }

    @Test
    @DisplayName("POST /users debe retornar 409 cuando el usuario ya existe")
    void shouldReturn409WhenUserAlreadyExists() throws Exception {
        // Given
        CreateUserRequest request = CreateUserRequest.builder()
                .name("Juan Pérez")
                .email("juan@example.com")
                .build();

        User domainUser = User.builder()
                .name("Juan Pérez")
                .email("juan@example.com")
                .build();

        when(userRestMapper.toDomain(any())).thenReturn(domainUser);
        when(createUserUseCase.createUser(any())).thenThrow(
                DomainException.conflict("USER_ALREADY_EXISTS", "El usuario con email juan@example.com ya existe")
        );

        // When & Then
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("USER_ALREADY_EXISTS"));
    }
}
