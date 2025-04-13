package projeto.hugo.terapia.professional.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projeto.hugo.terapia.professional.model.Language;

import java.util.UUID;

public interface ProfessionalLanguagesRepository extends JpaRepository<Language, UUID> {

    public Language findByValue(String value);

}
