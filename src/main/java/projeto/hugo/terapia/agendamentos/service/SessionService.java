package projeto.hugo.terapia.agendamentos.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import projeto.hugo.terapia.agendamentos.dto.ConfigAgendamentoDTO;
import projeto.hugo.terapia.agendamentos.dto.SessionRequestDTO;
import projeto.hugo.terapia.agendamentos.dto.SessionResponseDTO;
import projeto.hugo.terapia.agendamentos.enumeracoes.StatusSession;
import projeto.hugo.terapia.agendamentos.model.DateHourAgendamento;
import projeto.hugo.terapia.agendamentos.model.Session;
import projeto.hugo.terapia.agendamentos.repository.DateHourAgendamentoRepository;
import projeto.hugo.terapia.agendamentos.repository.SessionRepository;
import projeto.hugo.terapia.authentication.enumeracoes.StatusResponse;
import projeto.hugo.terapia.authentication.model.Usuario;
import projeto.hugo.terapia.authentication.service.UserService;
import projeto.hugo.terapia.authentication.utils.SecurityUtils;
import projeto.hugo.terapia.payment.model.Payment;
import projeto.hugo.terapia.payment.service.PaymentService;
import projeto.hugo.terapia.professional.model.Professional;
import projeto.hugo.terapia.professional.service.ProfessionalService;
import projeto.hugo.terapia.profile.model.Profile;
import projeto.hugo.terapia.profile.service.ProfileService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final UserService userService;
    private final SecurityUtils securityUtils;
    private final ProfessionalService professionalService;
    private final ProfileService profileService;
    private final ConfigAgendamentoService configAgendamentoService;
    private final DateHourAgendamentoRepository dateHourAgendamentoRepository;
    private final SessionRepository sessionRepository;
    private final PaymentService paymentService;

    public ResponseEntity<SessionResponseDTO> createSession(SessionRequestDTO sessionRequestDTO){
        UUID uuid = securityUtils.getIdUserByFilterSecurity();
        Usuario findUsuario = userService.findUserById(uuid);

        UUID idProfessional = sessionRequestDTO.idProfessional();
        if(findUsuario != null){
            Profile findProfile = profileService.findProfileByUser(findUsuario);

            Usuario findUsuarioProfessional = userService.findUserById(idProfessional);
            Professional professional = professionalService.findProfessionalByUser(findUsuarioProfessional);
            if(findProfile != null && professional != null){
                String hour = sessionRequestDTO.hour();
                LocalDateTime day = sessionRequestDTO.day();

                String dateTimeString = day.toLocalDate().toString() + "T" + hour + ":00"; // Formato "yyyy-MM-dd'T'HH:mm:ss"
                LocalDateTime localDateTime = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ISO_LOCAL_DATE_TIME);

                LocalDateTime dateNow = LocalDateTime.now();

                if(localDateTime.isBefore(dateNow)){
                    return ResponseEntity
                            .status(HttpStatus.UNAUTHORIZED).body(new SessionResponseDTO(
                                    StatusResponse.ERROR,
                                    "date-hour-past",
                                    "Você não pode agendar datas e horários no passado."
                            ));
                }

                DateHourAgendamento dateHourAgendamento =
                        dateHourAgendamentoRepository.findByDayHourAndProfessional(localDateTime, professional);

                if(dateHourAgendamento == null){
                    return ResponseEntity
                            .status(HttpStatus.UNAUTHORIZED).body(new SessionResponseDTO(
                                    StatusResponse.ERROR,
                                    "not-exists-date-hour",
                                    "A data e horário do agendamento solicitado não existe."
                            ));
                } else if(verifyIfSessionExistsByDateHourAndProfessional(dateHourAgendamento, professional)){
                    return ResponseEntity
                            .status(HttpStatus.UNAUTHORIZED).body(new SessionResponseDTO(
                                    StatusResponse.ERROR,
                                    "date-hour-has-been-marked",
                                    "A data e horário do agendamento nesse profissional " +
                                            "já está reservado por outro usuário."
                            ));
                }

                ConfigAgendamentoDTO configAgendamento = configAgendamentoService.extractConfigAgendamentoByAnyProfessional(professional.getUser().getId());

                if(configAgendamento == null){
                    return ResponseEntity
                            .status(HttpStatus.UNAUTHORIZED).body(new SessionResponseDTO(
                                    StatusResponse.ERROR,
                                    "not-exists-config-agendamento",
                                    "Esse profissional ainda não cadastrou as informações do seu agendamento. " +
                                            "Por favor, fale com o suporte."
                            ));
                }

                Session session = new Session();
                session.setProfessional(professional);
                session.setProfile(findProfile);
                session.setDateHourSession(dateHourAgendamento);
                session.setPayment(null);
                session.setStatus(StatusSession.PROCESSING);
                session.setDuration(configAgendamento.duration());
                Payment payment = paymentService.createPayment(configAgendamento.price());
                session.setPayment(payment);
                sessionRepository.save(session);


                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED).body(new SessionResponseDTO(
                                StatusResponse.SUCCESS,
                                "create-success",
                                "O seu agendamento foi criado com sucesso."
                        ));

            } else if(findProfile == null) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED).body(new SessionResponseDTO(
                                StatusResponse.ERROR,
                                "not-exists-profile",
                                "Não foi possível localizar o seu perfil. Por favor, fale com o suporte."
                        ));
            } else {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED).body(new SessionResponseDTO(
                                StatusResponse.ERROR,
                                "not-exists-professional",
                                "Não foi possível localizar esse profissional."
                        ));
            }
        }

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED).body(new SessionResponseDTO(
                        StatusResponse.ERROR,
                        "not-exists-user",
                        "Não foi possível localizar o seu usuário. Por favor, faça o login novamente."
                ));
    }

    public Boolean verifyIfSessionExistsByDateHourAndProfessional(DateHourAgendamento dateHourAgendamento, Professional professional){
        Session session = sessionRepository.findByDateHourSessionAndProfessional(dateHourAgendamento, professional);
        if(session == null){
            return false;
        } else return !session.getStatus().equals(StatusSession.CANCELLED);
    }

}
