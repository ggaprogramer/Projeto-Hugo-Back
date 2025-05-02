package projeto.hugo.terapia.agendamentos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projeto.hugo.terapia.agendamentos.model.ConfigAgendamento;
import projeto.hugo.terapia.professional.model.Professional;

import java.util.UUID;

public interface ConfigAgendamentoRepository extends JpaRepository<ConfigAgendamento, UUID>  {

    public ConfigAgendamento findByProfessional(Professional professional);

}
