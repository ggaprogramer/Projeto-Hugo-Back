package projeto.hugo.terapia.professional.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projeto.hugo.terapia.professional.model.Approach;

import java.util.UUID;

public interface ProfessionalApproachesRepository extends JpaRepository<Approach, UUID> {

    public Approach findByValue(String value);

}
