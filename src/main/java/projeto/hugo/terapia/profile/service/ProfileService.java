package projeto.hugo.terapia.profile.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import projeto.hugo.terapia.authentication.enumeracoes.StatusResponse;
import projeto.hugo.terapia.authentication.model.Usuario;
import projeto.hugo.terapia.authentication.service.UserService;
import projeto.hugo.terapia.authentication.utils.SecurityUtils;
import projeto.hugo.terapia.profile.dto.ProfileInfo;
import projeto.hugo.terapia.profile.dto.ProfileUpdateDTO;
import projeto.hugo.terapia.profile.dto.ResponseUpdateDTO;
import projeto.hugo.terapia.profile.enumeracoes.Gender;
import projeto.hugo.terapia.profile.enumeracoes.ProfileInterests;
import projeto.hugo.terapia.profile.enumeracoes.TypeProfile;
import projeto.hugo.terapia.profile.model.Profile;
import projeto.hugo.terapia.profile.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.format.DateTimeFormatter;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepositoryepository;
    private final UserService userService;
    private final SecurityUtils securityUtils;

    public void saveProfile(Profile profile) {
        profileRepositoryepository.save(profile);
    }

    public Profile findProfileByUser(Usuario usuario){
        return profileRepositoryepository.findByUser(usuario);
    }

    public ResponseEntity<ResponseUpdateDTO> updateProfile(ProfileUpdateDTO profileUpdateDTO) {
        UUID uuid = securityUtils.getIdUserByFilterSecurity();

        String name = profileUpdateDTO.name();
        String username = profileUpdateDTO.username();
        String password1 = profileUpdateDTO.password1();
        String password2 = profileUpdateDTO.password2();
        String email = profileUpdateDTO.email();
        String phone = profileUpdateDTO.phone();
        String dateBirth = profileUpdateDTO.dateBirth();
        List<ProfileInterests> interests = profileUpdateDTO.interests();
        Gender gender = profileUpdateDTO.gender();
        TypeProfile typeProfile = profileUpdateDTO.typeProfile();

        Usuario findUsuario = userService.encontrarPorId(uuid);
        if(findUsuario == null){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseUpdateDTO(
                            StatusResponse.ERROR,
                            "system",
                            "O identificador do usuário que foi enviado é inválido ou não existe. " +
                                    "Por favor, fale com o suporte."));
        }

        Boolean findUsuarioByUsername = userService.findUserByUsername(username);
        if(findUsuarioByUsername){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseUpdateDTO(
                            StatusResponse.ERROR,
                            "username",
                            "O usuário enviado já existe."));
        }

        Boolean findUsuarioByEmail = userService.findUserByEmail(email);
        if(findUsuarioByEmail){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseUpdateDTO(
                            StatusResponse.ERROR,
                            "email",
                            "O e-mail enviado já existe."));
        }

        Profile findProfile = findProfileByUser(findUsuario);
        if(findProfile == null){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseUpdateDTO(
                            StatusResponse.ERROR,
                            "system",
                            "O usuário logado não está atrelado a nenhum perfil. " +
                                    "Por favor, fale com o suporte."));
        }

        if (password1 != null && password2 != null) {
            if (!password1.equals(password2)) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new ResponseUpdateDTO(
                                StatusResponse.ERROR,
                                "password1",
                                "As senhas não são iguais."));
            }

            if (password1.length() < 8) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new ResponseUpdateDTO(
                                StatusResponse.ERROR,
                                "password1",
                                "A senha precisa ter no mínimo 8 caracteres."));
            }

            userService.updatePasswordUsuario(findUsuario, password1);
        }

        LocalDate dateFormated = LocalDate.parse(dateBirth);
        Integer age = this.calcularIdade(dateBirth);
        if(age < 15){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseUpdateDTO(
                            StatusResponse.ERROR,
                            "date-birth",
                            "Você tem que ter no mínimo 15 anos para acessar o site."));
        }

        findProfile.setName(name);
        findProfile.setPhone(phone);
        findProfile.setDateBirth(dateFormated);
        findProfile.setGender(gender);
        findProfile.setInterests(interests);
        saveProfile(findProfile);
        findUsuario.setUsername(username);
        findUsuario.setEmail(email);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseUpdateDTO(
                        StatusResponse.SUCCESS,
                        "O seu perfil foi atualizado com sucesso.",
                        "system"
                ));
    }

    public ResponseEntity<ProfileInfo> getInfoProfile(){

        UUID uuid = securityUtils.getIdUserByFilterSecurity();

        Usuario findUsuario = userService.encontrarPorId(uuid);
        if(findUsuario != null){
            Profile findProfile = findProfileByUser(findUsuario);
            if(findProfile != null){
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                String dataFormatada = findProfile.getDateBirth().format(formatter);

                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(new ProfileInfo(
                                findProfile.getName(),
                                findUsuario.getUsername(),
                                findUsuario.getEmail(),
                                findProfile.getPhone(),
                                dataFormatada,
                                findProfile.getInterests(),
                                findProfile.getGender(),
                                findUsuario.getConfirmacaoEmail()
                        ));
            }
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ProfileInfo(null, null, null,
                        null, null, null, null, null));
    }

    public int calcularIdade(String dataNascimento) {
        // Converter a string de dataNascimento para LocalDate
        LocalDate nascimento = LocalDate.parse(dataNascimento);
        LocalDate hoje = LocalDate.now(); // Data atual

        // Calcular a idade
        int idade = Period.between(nascimento, hoje).getYears();

        // Verifica se a pessoa já fez aniversário esse ano
        if (hoje.getMonthValue() < nascimento.getMonthValue() ||
                (hoje.getMonthValue() == nascimento.getMonthValue() && hoje.getDayOfMonth() < nascimento.getDayOfMonth())) {
            idade--; // Se não, diminui 1 ano
        }

        return idade;
    }
}
