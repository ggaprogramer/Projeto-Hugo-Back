package projeto.hugo.terapia.agendamentos.dto;

import projeto.hugo.terapia.agendamentos.enumeracoes.StatusSession;
import projeto.hugo.terapia.payment.enumeracoes.StatusPayment;
import projeto.hugo.terapia.profile.enumeracoes.Gender;

import java.time.LocalDateTime;
import java.util.UUID;

public record SessionProfessionalDTO(
        UUID id,
        String linkSession,
        UUID professionalId,
        String profileName,
        String linkPhotoProfile,
        Boolean active,
        Double amount,
        StatusPayment statusPayment,
        Integer duration,
        LocalDateTime dateHourSession,
        LocalDateTime dateHourSessionFinallized,
        StatusSession status,
        Gender gender
) {
}
