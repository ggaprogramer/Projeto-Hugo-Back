package projeto.hugo.terapia.professional.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import projeto.hugo.terapia.authentication.model.Usuario;
import projeto.hugo.terapia.professional.model.*;
import projeto.hugo.terapia.profile.enumeracoes.Gender;

import java.util.*;

public interface ProfessionalRepository extends JpaRepository<Professional, UUID> {

    public Optional<Professional> findByUser(Usuario usuario);

    public Professional findByCrp(String crp);

    public List<Professional> findByGender(Gender gender);

    public List<Professional> findByNameContainingIgnoreCase(String name);

    public List<Professional> findByInterests(List<ProfessionalInterests> interests);

    @Query("SELECT p FROM Professional p " +
            "LEFT JOIN p.interests i " +
            "WHERE i.id IN :interestsUUID")
    public List<Professional> findByInterestsIn(@Param("interestsUUID") List<UUID> interestsUUID);

    public List<Professional> findByApproaches(List<Approach> approaches);

    @Query("SELECT p FROM Professional p " +
            "LEFT JOIN p.approaches i " +
            "WHERE i.id IN :approachesUUID")
    public List<Professional> findByApproachesIn(@Param("approachesUUID") List<UUID> approachesUUID);

    public List<Professional> findBySpecialties(List<Specialty> specialties);

    @Query("SELECT p FROM Professional p " +
            "LEFT JOIN p.specialties i " +
            "WHERE i.id IN :specialtiesUUID")
    public List<Professional> findBySpecialtiesIn(@Param("specialtiesUUID") List<UUID> specialtiesUUID);

    public List<Professional> findByLanguages(List<Language> languages);

    @Query("SELECT p FROM Professional p " +
            "LEFT JOIN p.languages i " +
            "WHERE i.id IN :languagesUUID")
    public List<Professional> findByLanguagesIn(@Param("languagesUUID") List<UUID> languagesUUID);

}
