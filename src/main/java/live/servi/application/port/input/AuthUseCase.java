package live.servi.application.port.input;

import live.servi.domain.model.User;

/**
 * Puerto de entrada - Define el caso de uso
 * Esta es la interfaz que expone la logica de negocio
 */
public interface AuthUseCase {
    User createUser(User user);
}
