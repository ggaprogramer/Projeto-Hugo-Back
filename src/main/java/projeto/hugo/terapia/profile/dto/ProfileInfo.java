package projeto.hugo.terapia.profile.dto;

import projeto.hugo.terapia.profile.enumeracoes.Gender;

import java.util.List;

public record ProfileInfo(
        String name,
        String username,
        String email,
        String phone,
        String dateBirth,
        List<ProfileInterestsDTO> interests,
        Gender gender,
        Boolean confirmacaoEmail
) {
}
