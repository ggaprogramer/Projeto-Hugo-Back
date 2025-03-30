package projeto.hugo.terapia.professional.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import projeto.hugo.terapia.authentication.model.Usuario;
import projeto.hugo.terapia.professional.model.Professional;
import projeto.hugo.terapia.professional.repository.ProfessionalRepository;

@Service
@RequiredArgsConstructor
public class ProfessionalService {

    private final ProfessionalRepository professionalRepository;

    public void saveProfessional(Professional professional) {
        professionalRepository.save(professional);
    }

    public Professional findProfessionalByUser(Usuario usuario){
        return professionalRepository.findByUser(usuario);
    }

}
