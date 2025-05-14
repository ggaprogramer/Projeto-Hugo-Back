package projeto.hugo.terapia.agendamentos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projeto.hugo.terapia.agendamentos.model.DateHourAgendamento;
import projeto.hugo.terapia.professional.model.Professional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface DateHourAgendamentoRepository extends JpaRepository<DateHourAgendamento, UUID> {

    public List<DateHourAgendamento> findByProfessional(Professional professional);

    public DateHourAgendamento findByDayHourAndProfessional(LocalDateTime dayHour, Professional professional);

}
