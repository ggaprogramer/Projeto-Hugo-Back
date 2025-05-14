package projeto.hugo.terapia.professional.dto;

import projeto.hugo.terapia.agendamentos.dto.ConfigAgendamentoDTO;

public record ProfessionalAnyDTO (
        ProfessionalInfo professionalInfo,
        ConfigAgendamentoDTO configAgendamentoDTO
){
}
