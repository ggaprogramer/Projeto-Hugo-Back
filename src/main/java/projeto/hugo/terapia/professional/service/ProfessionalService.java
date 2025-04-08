package projeto.hugo.terapia.professional.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import projeto.hugo.terapia.authentication.model.Usuario;
import projeto.hugo.terapia.professional.model.Professional;
import projeto.hugo.terapia.professional.repository.ProfessionalRepository;
import projeto.hugo.terapia.profile.model.Profile;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfessionalService {

    private final ProfessionalRepository professionalRepository;

    public void saveProfessional(Professional professional) {
        professionalRepository.save(professional);
    }

    public Professional findProfessionalByUser(Usuario usuario){
        Optional<Professional> professional = professionalRepository.findByUser(usuario);
        return professional.orElse(null);
    }


    public Optional<Professional> findProfessionalById(UUID uuid){
        return professionalRepository.findById(uuid);
    }

    public Optional<Professional> findProfessionalById(String uuid){
        return professionalRepository.findById(UUID.fromString(uuid));
    }
}
