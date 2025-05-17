package projeto.hugo.terapia.agendamentos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projeto.hugo.terapia.agendamentos.model.DateHourAgendamento;
import projeto.hugo.terapia.agendamentos.model.Session;
import projeto.hugo.terapia.professional.model.Professional;
import projeto.hugo.terapia.profile.model.Profile;

import java.util.List;
import java.util.UUID;

public interface SessionRepository extends JpaRepository<Session, UUID> {

    Session findByDateHourSession(DateHourAgendamento dateHourAgendamento);

    List<Session> findByProfile(Profile profile);

    Session findByDateHourSessionAndProfessional(DateHourAgendamento dateHourAgendamento, Professional professional);
}
