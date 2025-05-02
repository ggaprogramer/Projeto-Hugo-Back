package projeto.hugo.terapia.agendamentos.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import projeto.hugo.terapia.agendamentos.model.ConfigAgendamento;
import projeto.hugo.terapia.agendamentos.repository.ConfigAgendamentoRepository;
import projeto.hugo.terapia.agendamentos.dto.ConfigAgendamentoDTO;
import projeto.hugo.terapia.authentication.model.Usuario;
import projeto.hugo.terapia.authentication.service.UserService;
import projeto.hugo.terapia.authentication.utils.SecurityUtils;
import projeto.hugo.terapia.professional.model.Professional;
import projeto.hugo.terapia.professional.service.ProfessionalService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConfigAgendamentoService {

    private final UserService userService;
    private final SecurityUtils securityUtils;
    private final ProfessionalService professionalService;
    private final ConfigAgendamentoRepository configAgendamentoRepository;

    public ResponseEntity<?> cadastrarConfigAgendamento(ConfigAgendamentoDTO configAgendamentoDTO){
        UUID uuid = securityUtils.getIdUserByFilterSecurity();
        Usuario findUsuario = userService.findUserById(uuid);

        if(findUsuario != null){
            Professional findProfessional = professionalService.findProfessionalByUser(findUsuario);
            if(findProfessional != null){

                ConfigAgendamento findConfigAgendamento = configAgendamentoRepository.findByProfessional(findProfessional);
                if(findConfigAgendamento == null){
                    ConfigAgendamento configAgendamento = new ConfigAgendamento();
                    configAgendamento.setDuration(configAgendamentoDTO.duration());
                    configAgendamento.setPrice(configAgendamentoDTO.price());
                    configAgendamentoRepository.save(configAgendamento);

                    return (ResponseEntity<?>) ResponseEntity
                            .status(HttpStatus.OK);
                }
            }
        }
        return (ResponseEntity<?>) ResponseEntity.status(HttpStatus.BAD_REQUEST);
    }

}
