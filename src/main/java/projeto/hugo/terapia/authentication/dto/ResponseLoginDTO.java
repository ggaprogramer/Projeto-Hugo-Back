package projeto.hugo.terapia.authentication.dto;

import projeto.hugo.terapia.authentication.enumeracoes.StatusResponse;
import org.springframework.http.HttpStatus;

public record ResponseLoginDTO (StatusResponse status, String message, String type, String token) {
}
