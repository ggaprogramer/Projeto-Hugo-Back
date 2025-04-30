package projeto.hugo.terapia.professional.dto;

import projeto.hugo.terapia.profile.enumeracoes.Gender;
import projeto.hugo.terapia.profile.enumeracoes.TypeProfile;

import java.util.List;

public record ProfessionalUpdateDTO (
        String name,
        String username,
        String password,
        String password1,
        String password2,
        String email,
        String phone,
        String description,
        String base64File,
        String mimeType,
        List<String> interests,
        List<String> approaches,
        List<String> specialties,
        List<String> languages,
        Gender gender,
        TypeProfile typeProfile
) {
}
