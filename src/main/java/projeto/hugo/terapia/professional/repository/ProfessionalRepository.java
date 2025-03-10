package projeto.hugo.terapia.professional.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projeto.hugo.terapia.authentication.model.Usuario;
import projeto.hugo.terapia.professional.model.Professional;

import java.util.UUID;

public interface ProfessionalRepository extends JpaRepository<Professional, UUID> {

    public Professional findByUser(Usuario usuario);

}
