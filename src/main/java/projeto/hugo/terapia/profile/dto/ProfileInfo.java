package projeto.hugo.terapia.profile.dto;

import projeto.hugo.terapia.profile.enumeracoes.Gender;
import projeto.hugo.terapia.profile.enumeracoes.ProfileInterests;
import projeto.hugo.terapia.profile.enumeracoes.TypeProfile;

import java.time.LocalDate;
import java.util.List;

public record ProfileInfo(
        String name,
        String username,
        String email,
        String phone,
        String dateBirth,
        List<ProfileInterests> interests,
        Gender gender,
        Boolean confirmacaoEmail
) {
}
