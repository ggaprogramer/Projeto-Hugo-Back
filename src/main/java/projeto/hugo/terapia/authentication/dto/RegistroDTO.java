package projeto.hugo.terapia.authentication.dto;
import projeto.hugo.terapia.authentication.enumeracoes.RolesUsers;
import projeto.hugo.terapia.profile.enumeracoes.Gender;
import projeto.hugo.terapia.profile.enumeracoes.TypeProfile;

import java.util.List;

public record RegistroDTO(
        String name,
        String username,
        String password1,
        String password2,
        String email,
        List<RolesUsers> roles,
        String phone,
        String dateBirth,
        List<String> interests,
        Gender gender,
        TypeProfile typeProfile){
}
