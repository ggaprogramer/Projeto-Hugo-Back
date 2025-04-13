package projeto.hugo.terapia.professional.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projeto.hugo.terapia.professional.model.Specialty;

import java.util.UUID;

public interface ProfessionalSpecialtiesRepository  extends JpaRepository<Specialty, UUID> {

    public Specialty findByValue(String value);

}
