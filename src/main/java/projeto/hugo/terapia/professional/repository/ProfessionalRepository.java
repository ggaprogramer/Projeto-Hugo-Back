package projeto.hugo.terapia.professional.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projeto.hugo.terapia.authentication.model.Usuario;
import projeto.hugo.terapia.professional.model.*;
import projeto.hugo.terapia.profile.enumeracoes.Gender;

import java.util.*;

public interface ProfessionalRepository extends JpaRepository<Professional, UUID> {

    public Optional<Professional> findByUser(Usuario usuario);

    public List<Professional> findByGender(Gender gender);

    public List<Professional> findByNameContainingIgnoreCase(String name);

    public List<Professional> findByInterests(List<ProfessionalInterests> interests);

    public List<Professional> findByApproaches(List<Approach> approaches);

    public List<Professional> findBySpecialties(List<Specialty> specialties);

    public List<Professional> findByLanguages(List<Language> languages);

}
