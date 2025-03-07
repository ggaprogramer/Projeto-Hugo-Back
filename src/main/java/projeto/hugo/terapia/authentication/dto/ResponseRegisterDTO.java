package projeto.hugo.terapia.authentication.dto;

import projeto.hugo.terapia.authentication.enumeracoes.StatusResponse;

public record ResponseRegisterDTO(StatusResponse status, String type, String message) {
}