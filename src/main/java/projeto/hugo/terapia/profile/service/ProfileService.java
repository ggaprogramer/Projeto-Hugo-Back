package projeto.hugo.terapia.profile.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import projeto.hugo.terapia.authentication.enumeracoes.StatusResponse;
import projeto.hugo.terapia.authentication.model.Usuario;
import projeto.hugo.terapia.authentication.service.UserService;
import projeto.hugo.terapia.authentication.utils.SecurityUtils;
import projeto.hugo.terapia.cloudfare.service.CloudfareService;
import projeto.hugo.terapia.profile.dto.*;
import projeto.hugo.terapia.profile.enumeracoes.Gender;
import projeto.hugo.terapia.profile.enumeracoes.TypeProfile;
import projeto.hugo.terapia.profile.model.Profile;
import projeto.hugo.terapia.profile.model.ProfilePhoto;
import projeto.hugo.terapia.profile.repository.ProfilePhotoRepository;
import projeto.hugo.terapia.profile.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

import java.time.LocalDate;
import java.time.Period;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileService {

    @Value("${spring.cloudfare.bucket-profile-photo-name}")
    private String bucketProfilePhotoName;

    private final ProfileRepository profileRepository;
    private final ProfileInterestsService profileInterestsService;
    private final UserService userService;
    private final SecurityUtils securityUtils;
    private final CloudfareService cloudfareService;

    public void saveProfile(Profile profile) {
        profileRepository.save(profile);
    }

    public Profile findProfileByUser(Usuario usuario){
        return profileRepository.findByUser(usuario);
    }

    public Optional<Profile> findProfileById(UUID uuid){
        return profileRepository.findById(uuid);
    }

    @Transactional
    public ResponseEntity<ResponseUpdateDTO> updateProfile(ProfileUpdateDTO profileUpdateDTO) {
        UUID uuid = securityUtils.getIdUserByFilterSecurity();

        String name = profileUpdateDTO.name();
        String username = profileUpdateDTO.username();
        String password = profileUpdateDTO.password();
        String password1 = profileUpdateDTO.password1();
        String password2 = profileUpdateDTO.password2();
        String email = profileUpdateDTO.email();
        String phone = profileUpdateDTO.phone();
        String base64File = profileUpdateDTO.base64File();
        String mimeType = profileUpdateDTO.mimeType();
        List<String> interests = profileUpdateDTO.interests();
        Gender gender = profileUpdateDTO.gender();
        TypeProfile typeProfile = profileUpdateDTO.typeProfile();

        if(!typeProfile.equals(TypeProfile.PROFILE)){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseUpdateDTO(
                            StatusResponse.ERROR,
                            "Apenas usuários comuns podem acessar essa área. " +
                                    "Por favor, fale com o suporte.",
                            "system"));
        }

        if(interests.isEmpty() || interests == null){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseUpdateDTO(
                            StatusResponse.ERROR,
                            "Você não pode deixar a lista de interesses vazia. Escolha no mínimo 1.",
                            "interests"));
        }

        Usuario findUsuario = userService.encontrarPorId(uuid);
        if(findUsuario == null){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseUpdateDTO(
                            StatusResponse.ERROR,
                            "O identificador do usuário que foi enviado é inválido ou não existe. " +
                                    "Por favor, fale com o suporte.",
                            "system"));
        }

        Boolean findUsuarioByUsername = userService.findUserByUsername(username);
        if(!findUsuario.getUsername().equals(username) && findUsuarioByUsername){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseUpdateDTO(
                            StatusResponse.ERROR,
                            "O usuário enviado já existe.",
                            "username"));
        }

        Boolean findUsuarioByEmail = userService.findUserByEmail(email);
        if(!findUsuario.getEmail().equals(email) && findUsuarioByEmail){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseUpdateDTO(
                            StatusResponse.ERROR,
                            "O e-mail enviado já existe.",
                            "email"));
        }

        Profile findProfile = findProfileByUser(findUsuario);
        if(findProfile == null){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseUpdateDTO(
                            StatusResponse.ERROR,
                            "O usuário logado não está atrelado a nenhum perfil. " +
                                    "Por favor, fale com o suporte.",
                            "system"));
        }

        if(!password.isEmpty() && !password1.isEmpty() && !password2.isEmpty()) {
            if(!userService.matchesPassword(password, findUsuario.getPassword())){
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new ResponseUpdateDTO(
                                StatusResponse.ERROR,
                                "A senha antiga enviada não é válida. Por favor, verificar novamente.",
                                "password"));
            }

            if(!password1.equals(password2)) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new ResponseUpdateDTO(
                                StatusResponse.ERROR,
                                "As senhas não são iguais.",
                                "password1"));
            }

            if(password1.length() < 8) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new ResponseUpdateDTO(
                                StatusResponse.ERROR,
                                "A senha precisa ter no mínimo 8 caracteres.",
                                "password1"));
            }

            userService.updatePasswordUsuario(findUsuario, password1);
        }

        ProfilePhoto profilePhoto;
        try{
            if(base64File != null || mimeType != null){
                if(findProfile.getPhoto() != null){
                    String nameFile = findProfile.getUser().getUsername() + "-" + UUID.randomUUID();
                    Boolean resultDeleteFile = cloudfareService.deleteFile(
                            bucketProfilePhotoName,
                            nameFile);
                    if(!resultDeleteFile){
                        throw new IOException("Erro ao deletar o arquivo. ");
                    } else {
                        ProfilePhoto profilePhotoDeleted = findProfile.getPhoto();
                        findProfile.setPhoto(null);
                        this.atualizarPerfil(findProfile);
                    }
                }

                String nameFile = findProfile.getUser().getUsername() + "-" + UUID.randomUUID();

                // Decodifica a string base64 em um array de bytes
                byte[] fileBytes = Base64.getDecoder().decode(base64File);
                // Cria um InputStream a partir dos bytes
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(fileBytes);

                Boolean resultUploadFile = cloudfareService.uploadFile(
                        bucketProfilePhotoName,
                        nameFile,
                        byteArrayInputStream,
                        mimeType,
                        true);
                if(!resultUploadFile){
                    throw new IOException("Erro ao salvar o arquivo. ");
                } else {
                    profilePhoto = new ProfilePhoto();
                    profilePhoto.setBucket(bucketProfilePhotoName);
                    profilePhoto.setName(nameFile);
                    profilePhoto.setProfile(findProfile);
                    findProfile.setPhoto(profilePhoto);
                    this.atualizarPerfil(findProfile);
                }
            }
        } catch (IOException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseUpdateDTO(
                            StatusResponse.ERROR,
                            e.getMessage() +
                                    "Por favor, fale com o suporte.",
                            "file"));
        }

        findProfile.setName(name);
        findProfile.setPhone(phone);
        findProfile.setGender(gender);
        findProfile.setInterests(profileInterestsService.getInterestsProfile(interests));
        findUsuario.setUsername(username);
        findUsuario.setEmail(email);
        userService.atualizarUsuario(findUsuario);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseUpdateDTO(
                        StatusResponse.SUCCESS,
                        "system",
                        "O seu perfil foi atualizado com sucesso."
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

                List<ProfileInterestsDTO> profileInterestsDTO = findProfile.getInterests()
                        .stream()
                        .map(interests ->
                                new ProfileInterestsDTO(interests.getValue(), interests.getLabel()))
                        .collect(Collectors.toList());

                String linkPhoto = null;
                if(findProfile.getPhoto() != null){
                    linkPhoto = cloudfareService.generateLinkFile(
                            findProfile.getPhoto().getBucket(),
                            findProfile.getPhoto().getName()
                    );
                }

                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(new ProfileInfo(
                                findProfile.getId(),
                                findProfile.getName(),
                                findUsuario.getUsername(),
                                findUsuario.getEmail(),
                                findProfile.getPhone(),
                                dataFormatada,
                                linkPhoto,
                                profileInterestsDTO,
                                findProfile.getGender(),
                                findUsuario.getConfirmacaoEmail()
                        ));
            }
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ProfileInfo(null, null, null, null,
                        null, null, null, null, null, null));
    }

    public ResponseEntity<ResponseUrlPhotoDTO> getUrlPhoto(@PathVariable UUID uuid){
        Optional<Profile> profile = this.findProfileById(uuid);
        if(profile.isPresent()){
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseUrlPhotoDTO(
                            cloudfareService.generateLinkFile(profile.get().getPhoto().getBucket(), profile.get().getPhoto().getName())
                    ));
        } else {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseUrlPhotoDTO(null));
        }
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

    public Profile atualizarPerfil(Profile profile){
        return profileRepository.save(profile);
    }
}
