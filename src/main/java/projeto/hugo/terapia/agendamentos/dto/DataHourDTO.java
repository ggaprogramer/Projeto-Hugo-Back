package projeto.hugo.terapia.agendamentos.dto;

import java.time.LocalDateTime;
import java.util.List;

public record DataHourDTO(LocalDateTime day, List<String> hours) {
}
