package projeto.hugo.terapia.professional.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import projeto.hugo.terapia.authentication.enumeracoes.StatusResponse;
import projeto.hugo.terapia.authentication.model.Usuario;
import projeto.hugo.terapia.authentication.service.UserService;
import projeto.hugo.terapia.authentication.utils.SecurityUtils;
import projeto.hugo.terapia.cloudfare.service.CloudfareService;
import projeto.hugo.terapia.professional.dto.*;
import projeto.hugo.terapia.professional.model.Professional;
import projeto.hugo.terapia.professional.model.ProfessionalInterests;
import projeto.hugo.terapia.professional.model.ProfessionalPhoto;
import projeto.hugo.terapia.professional.repository.ProfessionalRepository;
import projeto.hugo.terapia.profile.dto.*;
import projeto.hugo.terapia.profile.enumeracoes.Gender;
import projeto.hugo.terapia.profile.enumeracoes.TypeProfile;
import projeto.hugo.terapia.profile.enumeracoes.UnitSizeFile;
import projeto.hugo.terapia.profile.utils.ProfileUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfessionalService {

    @Value("${spring.cloudfare.bucket-professional-photo-name}")
    private String bucketProfessionalPhotoName;

    private final ProfessionalRepository professionalRepository;
    private final ProfessionalInterestsService professionalInterestsService;
    private final ProfessionalApproachesService professionalApproachesService;
    private final ProfessionalSpecialtiesService professionalSpecialtiesService;
    private final ProfessionalLanguagesService professionalLanguagesService;
    private final UserService userService;
    private final SecurityUtils securityUtils;
    private final CloudfareService cloudfareService;
    private final ProfileUtils profileUtils;

    public void saveProfessional(Professional professional) {
        professionalRepository.save(professional);
    }

    public Professional findProfessionalByUser(Usuario usuario){
        Optional<Professional> professional = professionalRepository.findByUser(usuario);
        return professional.orElse(null);
    }

    public Optional<Professional> findProfessionalById(UUID uuid){
        return professionalRepository.findById(uuid);
    }

    public Optional<Professional> findProfessionalById(String uuid){
        return professionalRepository.findById(UUID.fromString(uuid));
    }

    @Transactional
    public ResponseEntity<ResponseUpdateDTO> updateProfessional(ProfessionalUpdateDTO professionalUpdateDTO) {
        UUID uuid = securityUtils.getIdUserByFilterSecurity();

        String name = professionalUpdateDTO.name();
        String username = professionalUpdateDTO.username();
        String password = professionalUpdateDTO.password();
        String password1 = professionalUpdateDTO.password1();
        String password2 = professionalUpdateDTO.password2();
        String email = professionalUpdateDTO.email();
        String phone = professionalUpdateDTO.phone();
        String description = professionalUpdateDTO.description();
        String base64File = professionalUpdateDTO.base64File();
        String mimeType = professionalUpdateDTO.mimeType();
        List<String> interests = professionalUpdateDTO.interests();
        List<String> approaches = professionalUpdateDTO.approaches();
        List<String> Specialties = professionalUpdateDTO.specialties();
        List<String> languages = professionalUpdateDTO.languages();
        Gender gender = professionalUpdateDTO.gender();
        TypeProfile typeProfile = professionalUpdateDTO.typeProfile();

        if(!typeProfile.equals(TypeProfile.PROFESSIONAL)){
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

        Usuario findUsuario = userService.findUserById(uuid);
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

        Professional findProfessional = findProfessionalByUser(findUsuario);
        if(findProfessional == null){
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

        ProfessionalPhoto professionalPhoto;
        try{
            if(base64File != null || mimeType != null){
                Integer sizeFileFormated = profileUtils.calculateSizeFileInBytes(base64File);
                Double sizeMax = 5.0;
                UnitSizeFile sizeSymbol = UnitSizeFile.MB;
                Double sizeMaxBytes = profileUtils.convertToBytes(sizeMax, sizeSymbol);

                if(sizeFileFormated >= sizeMaxBytes){
                    throw new IOException("O tamanho da foto tem que ser menor do que " + sizeMax + " " + sizeSymbol.getDescricao());
                }

                if(findProfessional.getPhoto() != null){
                    Boolean resultDeleteFile = cloudfareService.deleteFile(
                            findProfessional.getPhoto().getBucket(),
                            findProfessional.getPhoto().getName());
                    if(!resultDeleteFile){
                        throw new IOException("Erro ao deletar o arquivo. Por favor, fale com o suporte.");
                    } else {
                        findProfessional.setPhoto(null);
                        this.atualizarProfessional(findProfessional);
                    }
                }

                String nameFile = findProfessional.getUser().getUsername() + "-" + UUID.randomUUID();

                // Decodifica a string base64 em um array de bytes
                byte[] fileBytes = Base64.getDecoder().decode(base64File);
                // Cria um InputStream a partir dos bytes
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(fileBytes);

                Boolean resultUploadFile = cloudfareService.uploadFile(
                        bucketProfessionalPhotoName,
                        nameFile,
                        byteArrayInputStream,
                        mimeType);
                if(!resultUploadFile){
                    throw new IOException("Erro ao salvar o arquivo. Por favor, fale com o suporte.");
                } else {
                    professionalPhoto = new ProfessionalPhoto();
                    professionalPhoto.setBucket(bucketProfessionalPhotoName);
                    professionalPhoto.setName(nameFile);
                    professionalPhoto.setProfessional(findProfessional);
                    findProfessional.setPhoto(professionalPhoto);
                    this.atualizarProfessional(findProfessional);
                }
            }
        } catch (IOException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseUpdateDTO(
                            StatusResponse.ERROR,
                            e.getMessage(),
                            "file"));
        }

        findProfessional.setName(name);
        findProfessional.setPhone(phone);
        findProfessional.setGender(gender);
        findProfessional.setDescription(description);
        findProfessional.setInterests(professionalInterestsService.getInterestsProfessional(interests));
        findProfessional.setApproaches(professionalApproachesService.getApproachesProfessional(approaches));
        findProfessional.setSpecialties(professionalSpecialtiesService.getSpecialtiesProfessional(Specialties));
        findProfessional.setLanguages(professionalLanguagesService.getLanguagesProfessional(languages));
        findUsuario.setUsername(username);
        findUsuario.setEmail(email);
        userService.atualizarUsuario(findUsuario);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseUpdateDTO(
                        StatusResponse.SUCCESS,
                        "system",
                        "O seu perfil de profissional foi atualizado com sucesso."
                ));
    }

    public ResponseEntity<ProfessionalInfo> getInfoProfessional(){

        UUID uuid = securityUtils.getIdUserByFilterSecurity();

        Usuario findUsuario = userService.findUserById(uuid);
        if(findUsuario != null){
            Professional findProfessional = findProfessionalByUser(findUsuario);
            if(findProfessional != null){
                ProfessionalInfo professionalInfo = this.professionalToDTO(findProfessional);

                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(professionalInfo);
            }
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ProfessionalInfo(null, null, null, null,
                        null, null, null, null, null, null,
                        null, null, null, null, null, null));
    }

    public ResponseEntity<ResponseUrlPhotoDTO> getUrlPhoto(@PathVariable UUID uuid){
        Optional<Professional> professional = this.findProfessionalById(uuid);
        if(professional.isPresent()){
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseUrlPhotoDTO(
                            cloudfareService.generateLinkFile(professional.get().getPhoto().getBucket(), professional.get().getPhoto().getName())
                    ));
        } else {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseUrlPhotoDTO(null));
        }
    }

    public Professional atualizarProfessional(Professional professional){
        return professionalRepository.save(professional);
    }

    public ProfessionalInfo professionalToDTO(Professional professional){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dataFormatada = professional.getDateBirth().format(formatter);

        List<ProfessionalInterestsDTO> professionalInterests = professional.getInterests()
                .stream()
                .map(interest ->
                        new ProfessionalInterestsDTO(interest.getValue(), interest.getLabel()))
                .collect(Collectors.toList());

        List<ProfessionalApproachDTO> professionalApproachs = professional.getApproaches()
                .stream()
                .map(approach ->
                        new ProfessionalApproachDTO(approach.getLabel(), approach.getValue()))
                .collect(Collectors.toList());

        List<ProfessionalSpecialtyDTO> professionalSpecialties = professional.getSpecialties()
                .stream()
                .map(speciality ->
                        new ProfessionalSpecialtyDTO(speciality.getLabel(), speciality.getValue()))
                .collect(Collectors.toList());

        List<ProfessionalLanguageDTO> professionalLanguages = professional.getLanguages()
                .stream()
                .map(language ->
                        new ProfessionalLanguageDTO(language.getValue(), language.getLabel(), language.getLevel().name()))
                .collect(Collectors.toList());

        String linkPhoto = null;
        if(professional.getPhoto() != null){
            linkPhoto = cloudfareService.generateLinkFile(
                    professional.getPhoto().getBucket(),
                    professional.getPhoto().getName()
            );
        }

        return new ProfessionalInfo(
                professional.getId(),
                professional.getName(),
                professional.getUser().getUsername(),
                professional.getUser().getEmail(),
                professional.getPhone(),
                professional.getCrp(),
                professional.getDescription(),
                dataFormatada,
                linkPhoto,
                professionalInterests,
                professionalApproachs,
                professionalSpecialties,
                professionalLanguages,
                professional.getGender(),
                professional.getUser().getConfirmacaoEmail(),
                professional.getRegistrationCompleted()
        );
    }

    public Page<ProfessionalInfo> listFilterProfessionals(ProfessionalFilterDTO professionalFilterDTO) {
        Integer pagina = professionalFilterDTO.pagina();
        Integer tamanho = professionalFilterDTO.tamanho();
        String direcao = professionalFilterDTO.direcao();
        String ordenarPor = professionalFilterDTO.ordenarPor();
        String nome = professionalFilterDTO.nome();
        List<String> abordagens = professionalFilterDTO.abordagens();
        List<String> especialidades = professionalFilterDTO.especialidades();
        List<String> interesses = professionalFilterDTO.interesses();
        List<String> idiomas = professionalFilterDTO.idiomas();
        Double precoMinimo = professionalFilterDTO.precoMinimo();
        Double precoMaximo = professionalFilterDTO.precoMaximo();
        GenderFilter genero = professionalFilterDTO.genero();
        List<Disponibilidade> disponibilidades = professionalFilterDTO.disponibilidade();

        Set<Professional> professionals = new HashSet<>();
        professionals.addAll(professionalRepository.findAll());

        if (interesses != null && !interesses.isEmpty()) {
            professionals = professionals
                    .stream()
                    .filter(professional -> new HashSet<>(professional.getInterests())
                            .containsAll(professionalInterestsService.getInterestsProfessional(interesses)))
                    .collect(Collectors.toSet());
        }

        if (abordagens != null && !abordagens.isEmpty()) {
            professionals = professionals
                    .stream()
                    .filter(professional -> new HashSet<>(professional.getApproaches())
                            .containsAll(professionalApproachesService.getApproachesProfessional(abordagens)))
                    .collect(Collectors.toSet());
        }

        if (especialidades != null && !especialidades.isEmpty()) {
            professionals = professionals
                    .stream()
                    .filter(professional -> new HashSet<>(professional.getSpecialties())
                            .containsAll(professionalSpecialtiesService.getSpecialtiesProfessional(especialidades)))
                    .collect(Collectors.toSet());
        }

        if (idiomas != null && !idiomas.isEmpty()) {
            professionals = professionals
                    .stream()
                    .filter(professional -> new HashSet<>(professional.getLanguages())
                            .containsAll(professionalLanguagesService.getLanguagesProfessional(idiomas)))
                    .collect(Collectors.toSet());
        }

        if (nome != null && !nome.isBlank()) {
            professionals = professionals
                    .stream()
                    .filter(professional -> professional.getName().toLowerCase().contains(nome.toLowerCase()))
                    .collect(Collectors.toSet());
        }

        if (genero != null && !genero.equals(GenderFilter.TODOS)) {
            Gender gender;
            if(genero == GenderFilter.MASCULINO) {
                gender = Gender.MASCULINO;
            } else if(genero == GenderFilter.FEMININO) {
                gender = Gender.FEMININO;
            } else {
                gender = Gender.OUTROS;
            }

            professionals = professionals
                    .stream()
                    .filter(professional -> professional.getGender().equals(gender))
                    .collect(Collectors.toSet());

        }

        Pageable pageable = PageRequest.of(pagina, tamanho, Sort.by(Sort.Direction.fromString(direcao), ordenarPor));

        List<ProfessionalInfo> professionalsInfo = professionals.stream()
                .filter(Professional::getRegistrationCompleted)
                .map(this::professionalToDTO)
                .collect(Collectors.toList());

        if (pageable.getSort().isSorted()) {
            for (var order : pageable.getSort()) {
                Comparator<ProfessionalInfo> comparator = getComparator(order.getProperty());

                if (comparator != null) {
                    if (order.isDescending()) {
                        comparator = comparator.reversed();
                    }
                    professionalsInfo = professionalsInfo.stream()
                            .sorted(comparator)
                            .collect(Collectors.toList());
                }
            }
        }

        // 4. Paginar na memória
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<ProfessionalInfo> pagedList;

        if (professionalsInfo.size() < startItem) {
            pagedList = List.of();
        } else {
            int toIndex = Math.min(startItem + pageSize, professionalsInfo.size());
            pagedList = professionalsInfo.subList(startItem, toIndex);
        }

        return new PageImpl<>(pagedList, pageable, professionalsInfo.size());
    }

    private Comparator<ProfessionalInfo> getComparator(String property) {
        return switch (property) {
            case "id" -> Comparator.comparing(ProfessionalInfo::uuid);
            case "name" -> Comparator.comparing(ProfessionalInfo::name, String.CASE_INSENSITIVE_ORDER);
            case "email" -> Comparator.comparing(ProfessionalInfo::email, String.CASE_INSENSITIVE_ORDER);
            default -> null;
        };
    }

    public Professional findByCrp(String crp){
        return professionalRepository.findByCrp(crp);
    }
}
