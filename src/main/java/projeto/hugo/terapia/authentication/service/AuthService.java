package projeto.hugo.terapia.authentication.service;

import projeto.hugo.terapia.authentication.dto.*;
import projeto.hugo.terapia.authentication.enumeracoes.RolesUsers;
import projeto.hugo.terapia.authentication.enumeracoes.StatusResponse;
import projeto.hugo.terapia.authentication.model.Usuario;
import projeto.hugo.terapia.authentication.security.TokenService;
import projeto.hugo.terapia.professional.model.Professional;
import projeto.hugo.terapia.professional.service.ProfessionalInterestsService;
import projeto.hugo.terapia.professional.service.ProfessionalService;
import projeto.hugo.terapia.profile.enumeracoes.Gender;
import projeto.hugo.terapia.profile.enumeracoes.TypeProfile;
import projeto.hugo.terapia.profile.model.Profile;
import projeto.hugo.terapia.profile.service.ProfileInterestsService;
import projeto.hugo.terapia.profile.service.ProfileService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final PasswordEncoder encoder;
    private final TokenService tokenService;
    private final ProfileService profileService;
    private final ProfessionalService professionalService;
    private final ProfileInterestsService profileInterestsService;
    private final ProfessionalInterestsService professionalInterestsService;

    public ResponseEntity<ResponseLoginDTO> login(LoginDTO loginDTO, HttpServletResponse response){
        String email = loginDTO.email();
        String password = loginDTO.password();
        Boolean lembrarSenha = loginDTO.lembrarSenha();
        Integer expirationToken = 1;

        if(email == null){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseLoginDTO(
                            StatusResponse.ERROR,
                            "Nenhum e-mail foi enviado.",
                            "email", null, null
                    ));
        }

        if(password == null){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseLoginDTO(
                            StatusResponse.ERROR,
                            "Nenhuma senha foi enviada.",
                            "password", null, null
                    ));
        }

        Usuario usuario = userService.encontrarPorEmail(email);
        if(usuario == null){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ResponseLoginDTO(
                            StatusResponse.ERROR,
                            "O e-mail enviado não está atrelado a nenhum usuário.",
                            "email", null, null
                    ));
        }

        Profile profile = profileService.findProfileByUser(usuario);
        Professional professional = null;
        if(profile == null){
            professional = professionalService.findProfessionalByUser(usuario);
        }
        if(profile == null && professional == null){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ResponseLoginDTO(
                            StatusResponse.ERROR,
                            "Esse usuário não está ligado a nenhum perfil e a nenhum profissional. " +
                                    "Por favor, fale com o suporte.",
                            "system", null, null
                    ));
        }

        TypeProfile typeProfile = TypeProfile.PROFILE;
        if(profile == null){
            typeProfile = TypeProfile.PROFESSIONAL;
        }

        String senhaCriptografada = usuario.getPassword();
        boolean senhasBatem = encoder.matches(password, senhaCriptografada);

        if(!senhasBatem){
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(new ResponseLoginDTO(
                            StatusResponse.ERROR,
                            "Acesso negado. A senha está incorreta.",
                            "password", null, null
                    ));
        }

        if(lembrarSenha){
            expirationToken = 5;
        }

        try {
            String token = tokenService.generateToken(usuario, expirationToken);
            usuario.setLastLogin(LocalDateTime.now());
            userService.atualizarUsuario(usuario);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseLoginDTO(
                            StatusResponse.SUCCESS,
                            "Login feito com sucesso.", null, token, typeProfile));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseLoginDTO(
                            StatusResponse.ERROR,
                            "Erro ao gerar o token.",
                            "system", null, null));
        }
    }

    public ResponseEntity<ResponseRegisterDTO> registro(RegistroDTO registroDTO){
        String name = registroDTO.name();
        String username = registroDTO.username();
        String email = registroDTO.email();
        String password1 = registroDTO.password1();
        String password2 = registroDTO.password2();
        List<RolesUsers> roles = registroDTO.roles();
        String phone = registroDTO.phone();
        String dateBirth = registroDTO.dateBirth();
        List<String> interests = registroDTO.interests();
        Gender gender = registroDTO.gender();
        TypeProfile typeProfile = registroDTO.typeProfile();

        if(name == null){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseRegisterDTO(
                            StatusResponse.ERROR,
                            "name",
                            "Nenhum nome foi enviado."));
        }

        if(username == null){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseRegisterDTO(
                            StatusResponse.ERROR,
                            "username",
                            "Nenhum username foi enviado."));
        }

        if(email == null){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseRegisterDTO(
                            StatusResponse.ERROR,
                            "email",
                            "Nenhum email foi enviado."));
        }

        if(password1 == null || password2 == null){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseRegisterDTO(
                            StatusResponse.ERROR,
                            "passwords",
                            "As duas senhas não foram preenchidas."));
        }

        if(roles == null || roles.isEmpty()){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseRegisterDTO(
                            StatusResponse.ERROR,
                            "roles",
                            "Nenhuma permissão foi enviada."));
        }

        if(username.length() < 5){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseRegisterDTO(
                            StatusResponse.ERROR,
                            "username",
                            "O username deve ter no mínimo 5 caracteres."));
        }

        if(phone == null){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseRegisterDTO(
                            StatusResponse.ERROR,
                            "phone",
                            "Nenhum número de celular foi enviado."));
        }

        if(dateBirth == null){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseRegisterDTO(
                            StatusResponse.ERROR,
                            "date-birth",
                            "Nenhuma data de aniversário foi enviada."));
        }

        if(interests == null || interests.isEmpty()){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseRegisterDTO(
                            StatusResponse.ERROR,
                            "interests",
                            "Nenhum interesse foi enviado."));
        }

        if(gender == null){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseRegisterDTO(
                            StatusResponse.ERROR,
                            "gender",
                            "Nenhum gênero foi enviado."));
        }

        if(typeProfile == null){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseRegisterDTO(
                            StatusResponse.ERROR,
                            "type-profile",
                            "Nenhum tipo de perfil foi enviado."));
        }

        Usuario usuarioEmail = userService.encontrarPorEmail(email);
        if(usuarioEmail != null){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseRegisterDTO(
                            StatusResponse.ERROR,
                            "email",
                            "Esse e-mail já está sendo usado."));
        }

        Usuario usuarioUsername = userService.encontrarPorUsername(username);
        if(usuarioUsername != null){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseRegisterDTO(
                            StatusResponse.ERROR,
                            "username",
                            "Esse nome de usuário já está sendo usado."));
        }

        if(!password1.equals(password2)){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseRegisterDTO(
                            StatusResponse.ERROR,
                            "passwords",
                            "As senhas não são iguais."));
        }

        if(password1.length() < 8){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseRegisterDTO(
                            StatusResponse.ERROR,
                            "password",
                            "A senha precisa ter no mínimo 8 caracteres."));
        }

        LocalDate dateFormated = LocalDate.parse(dateBirth);

        Integer age = profileService.calcularIdade(dateBirth);
        if(age < 15) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseRegisterDTO(
                            StatusResponse.ERROR,
                            "date-birth",
                            "Você tem que ter no mínimo 15 anos para acessar o site."));
        }

        Usuario novoUsuario = userService.salvarUsuario(registroDTO);
        if(typeProfile.equals(TypeProfile.PROFILE)){
            Profile profile = new Profile();
            profile.setUser(novoUsuario);
            profile.setName(name);
            profile.setPhone(phone);
            profile.setDateBirth(dateFormated);
            profile.setGender(gender);
            profile.setInterests(profileInterestsService.getInterestsProfile(interests));
            profileService.saveProfile(profile);
        } else {
            Professional professional = new Professional();
            professional.setUser(novoUsuario);
            professional.setName(name);
            professional.setPhone(phone);
            professional.setDateBirth(dateFormated);
            professional.setGender(gender);
            professional.setInterests(professionalInterestsService.getInterestsProfessional(interests));
            professionalService.saveProfessional(professional);
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseRegisterDTO(
                        StatusResponse.SUCCESS,
                        null,
                        "Perfil criado com sucesso."));
    }

    public ResponseEntity<isAuthenticatedResponseDTO> isAuthenticated(isAuthenticatedDTO isAuthenticatedDTO) {
        String token = isAuthenticatedDTO.token();
        if(token != null){
            String idUser = tokenService.validateToken(token);
            if(idUser != null) {
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(new isAuthenticatedResponseDTO(token));
            }
        };
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new isAuthenticatedResponseDTO(null));
    }
}
