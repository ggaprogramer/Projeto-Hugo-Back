package projeto.hugo.terapia.agendamentos.dto;

import projeto.hugo.terapia.agendamentos.enumeracoes.StatusSession;

import java.time.LocalDateTime;

public record SessionFilterProfileDTO(
        Integer pagina,
        Integer tamanho,
        String ordenarPor,
        String direcao,
        String nomeProfessional,
        LocalDateTime date,
        StatusSession status
) {
}
