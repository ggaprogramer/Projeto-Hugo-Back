package projeto.hugo.terapia.professional.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projeto.hugo.terapia.professional.model.ProfessionalInterests;
import projeto.hugo.terapia.profile.model.ProfileInterests;

import java.util.UUID;

public interface ProfessionalInterestsRepository extends JpaRepository<ProfessionalInterests, UUID> {

    public ProfessionalInterests findByValue(String value);

}
