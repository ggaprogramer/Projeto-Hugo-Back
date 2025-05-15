package projeto.hugo.terapia.agendamentos.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record SessionRequestDTO(
        UUID idProfessional,
        LocalDateTime day,
        String hour
) {
}
