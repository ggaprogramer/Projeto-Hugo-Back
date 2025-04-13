package projeto.hugo.terapia.professional.dto;

import projeto.hugo.terapia.profile.enumeracoes.Gender;

import java.util.List;
import java.util.UUID;

public record ProfessionalInfo(
        UUID uuid,
        String name,
        String username,
        String email,
        String phone,
        String dateBirth,
        String linkPhoto,
        List<ProfessionalInterestsDTO> interests,
        List<ProfessionalApproachDTO> approaches,
        List<ProfessionalSpecialtyDTO> specialties,
        List<ProfessionalLanguageDTO> languages,
        Gender gender,
        Boolean confirmacaoEmail,
        Boolean registrationCompleted
) {
}
