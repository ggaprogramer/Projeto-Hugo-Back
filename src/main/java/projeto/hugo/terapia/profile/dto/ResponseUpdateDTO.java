package projeto.hugo.terapia.profile.dto;

import projeto.hugo.terapia.authentication.enumeracoes.StatusResponse;

public record ResponseUpdateDTO (StatusResponse status, String message, String type) {
}
