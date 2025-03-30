package projeto.hugo.terapia.profile.dto;

import projeto.hugo.terapia.profile.enumeracoes.Gender;
import projeto.hugo.terapia.profile.enumeracoes.TypeProfile;
import projeto.hugo.terapia.profile.model.ProfileInterests;

import java.util.List;

public record ProfileUpdateDTO(
        String name,
        String username,
        String password1,
        String password2,
        String email,
        String phone,
        String dateBirth,
        List<ProfileInterests> interests,
        Gender gender,
        TypeProfile typeProfile
        ) {
}
