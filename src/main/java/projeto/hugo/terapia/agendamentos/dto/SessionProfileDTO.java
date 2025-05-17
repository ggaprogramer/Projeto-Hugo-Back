package projeto.hugo.terapia.agendamentos.dto;

import projeto.hugo.terapia.agendamentos.enumeracoes.StatusSession;
import projeto.hugo.terapia.payment.enumeracoes.StatusPayment;

import java.time.LocalDateTime;
import java.util.UUID;

public record SessionProfileDTO(
        UUID id,
        String linkSession,
        UUID professionalId,
        String professionalName,
        String linkPhotoProfessional,
        Boolean active,
        Double amount,
        StatusPayment statusPayment,
        Integer duration,
        LocalDateTime dateHourSession,
        LocalDateTime dateHourSessionFinallized,
        StatusSession status
) {
}
