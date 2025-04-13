package projeto.hugo.terapia.profile.dto;

import projeto.hugo.terapia.profile.enumeracoes.Gender;

import java.util.List;
import java.util.UUID;

public record ProfileInfo(
        UUID uuid,
        String name,
        String username,
        String email,
        String phone,
        String dateBirth,
        String linkPhoto,
        List<ProfileInterestsDTO> interests,
        Gender gender,
        Boolean confirmacaoEmail
) {
}
