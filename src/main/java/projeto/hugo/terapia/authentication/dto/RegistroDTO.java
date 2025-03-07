package projeto.hugo.terapia.authentication.dto;
import projeto.hugo.terapia.authentication.enumeracoes.RolesUsers;
import projeto.hugo.terapia.profile.enumeracoes.ProfileInterests;

import java.util.List;

public record RegistroDTO(
        String username,
        String password1,
        String password2,
        String email,
        List<RolesUsers> roles,
        String phone,
        String age,
        List<ProfileInterests> interests){
}
