package projeto.hugo.terapia.agendamentos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projeto.hugo.terapia.agendamentos.model.DateHourAgendamento;
import projeto.hugo.terapia.agendamentos.model.Session;
import projeto.hugo.terapia.professional.model.Professional;

import java.util.UUID;

public interface SessionRepository extends JpaRepository<Session, UUID> {

    Session findByDateHourSession(DateHourAgendamento dateHourAgendamento);

    Session findByDateHourSessionAndProfessional(DateHourAgendamento dateHourAgendamento, Professional professional);
}
