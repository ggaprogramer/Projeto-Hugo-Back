package projeto.hugo.terapia.profile.dto;

import projeto.hugo.terapia.profile.enumeracoes.Gender;
import projeto.hugo.terapia.profile.enumeracoes.TypeProfile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record ProfileUpdateDTO(
        String name,
        String username,
        String password,
        String password1,
        String password2,
        String email,
        String phone,
        String base64File,
        String mimeType,
        List<String> interests,
        Gender gender,
        TypeProfile typeProfile
        ) {
}
