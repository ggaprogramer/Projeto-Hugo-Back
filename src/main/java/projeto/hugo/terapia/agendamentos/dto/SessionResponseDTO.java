package projeto.hugo.terapia.agendamentos.dto;

import projeto.hugo.terapia.authentication.enumeracoes.StatusResponse;

public record SessionResponseDTO(
        StatusResponse status,
        String type,
        String message
) {
}
