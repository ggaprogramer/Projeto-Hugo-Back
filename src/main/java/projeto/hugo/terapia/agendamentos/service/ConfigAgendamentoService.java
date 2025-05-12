package projeto.hugo.terapia.agendamentos.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import projeto.hugo.terapia.agendamentos.dto.DataHourDTO;
import projeto.hugo.terapia.agendamentos.model.ConfigAgendamento;
import projeto.hugo.terapia.agendamentos.model.DateHourAgendamento;
import projeto.hugo.terapia.agendamentos.repository.ConfigAgendamentoRepository;
import projeto.hugo.terapia.agendamentos.dto.ConfigAgendamentoDTO;
import projeto.hugo.terapia.agendamentos.repository.DateHourAgendamentoRepository;
import projeto.hugo.terapia.authentication.model.Usuario;
import projeto.hugo.terapia.authentication.service.UserService;
import projeto.hugo.terapia.authentication.utils.SecurityUtils;
import projeto.hugo.terapia.professional.model.Professional;
import projeto.hugo.terapia.professional.service.ProfessionalService;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.time.ZoneId;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConfigAgendamentoService {

    private final UserService userService;
    private final SecurityUtils securityUtils;
    private final ProfessionalService professionalService;
    private final ConfigAgendamentoRepository configAgendamentoRepository;
    private final DateHourAgendamentoRepository dateHourAgendamentoRepository;

    public ResponseEntity<ConfigAgendamentoDTO> extractConfigAgendamento(){
        UUID uuid = securityUtils.getIdUserByFilterSecurity();
        Usuario findUsuario = userService.findUserById(uuid);

        if(findUsuario != null){
            Professional findProfessional = professionalService.findProfessionalByUser(findUsuario);
            if(findProfessional != null){

                ConfigAgendamento findConfigAgendamento = configAgendamentoRepository.findByProfessional(findProfessional);
                if(findConfigAgendamento != null){
                    ConfigAgendamentoDTO configAgendamentoDTO = new ConfigAgendamentoDTO(
                            findConfigAgendamento.getPrice(),
                            findConfigAgendamento.getDuration()
                    );

                    return ResponseEntity
                            .status(HttpStatus.OK)
                            .body(configAgendamentoDTO);
                }
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    public ResponseEntity<Boolean> cadastrarOuAtualizarConfigAgendamento(ConfigAgendamentoDTO configAgendamentoDTO){
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
                    configAgendamento.setProfessional(findProfessional);
                    configAgendamentoRepository.save(configAgendamento);
                } else {
                    findConfigAgendamento.setDuration(configAgendamentoDTO.duration());
                    findConfigAgendamento.setPrice(configAgendamentoDTO.price());
                    configAgendamentoRepository.save(findConfigAgendamento);
                }

                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(true);
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
    }

    public ResponseEntity<Boolean> cadastrarDateHourAgendamento(DataHourDTO dataHourDTO){
        UUID uuid = securityUtils.getIdUserByFilterSecurity();
        Usuario findUsuario = userService.findUserById(uuid);

        if(findUsuario != null){
            Professional findProfessional = professionalService.findProfessionalByUser(findUsuario);
            if(findProfessional != null){
                List<String> hours = dataHourDTO.hours();
                LocalDateTime day = dataHourDTO.day();

                for (String hour : hours) {
                    String dateTimeString = day.toLocalDate().toString() + "T" + hour + ":00"; // Formato "yyyy-MM-dd'T'HH:mm:ss"
                    LocalDateTime localDateTime = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ISO_LOCAL_DATE_TIME);

                    LocalDateTime dateNow = LocalDateTime.now();

                    if(localDateTime.isBefore(dateNow)){
                        return ResponseEntity
                                .status(HttpStatus.UNAUTHORIZED).body(false);
                    }

                    DateHourAgendamento verifyIfExistsDateHourAgendamento =
                            dateHourAgendamentoRepository.findByDayHourAndProfessional(localDateTime, findProfessional);

                    if(verifyIfExistsDateHourAgendamento == null){
                        DateHourAgendamento dateHourAgendamento = new DateHourAgendamento();
                        dateHourAgendamento.setDayHour(localDateTime);
                        dateHourAgendamento.setProfessional(findProfessional);
                        dateHourAgendamentoRepository.save(dateHourAgendamento);
                    } else {
                        return ResponseEntity
                                .status(HttpStatus.UNAUTHORIZED).body(false);
                    }
                }

                return ResponseEntity
                        .status(HttpStatus.OK).body(true);
            }
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
    }

    public ResponseEntity<List<DataHourDTO>> getDateHourAgendamento(){
        UUID uuid = securityUtils.getIdUserByFilterSecurity();
        Usuario findUsuario = userService.findUserById(uuid);

        if(findUsuario != null){
            Professional findProfessional = professionalService.findProfessionalByUser(findUsuario);
            if(findProfessional != null){
                List<DateHourAgendamento> listDateHourAgendamento = dateHourAgendamentoRepository.findByProfessional(findProfessional);
                List<LocalDateTime> dateTimes = listDateHourAgendamento
                        .stream()
                        .map(DateHourAgendamento::getDayHour)
                        .toList();
                List<DataHourDTO> listDataHourDTO = toDataHourDTO(dateTimes);

                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(listDataHourDTO);
            }
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    private List<DataHourDTO> toDataHourDTO(List<LocalDateTime> dateTimes) {
        if (dateTimes == null || dateTimes.isEmpty()) {
            return List.of(); // Retorna uma lista vazia se a entrada for nula ou vazia
        }

        // Agrupar os LocalDateTime por data (removendo a parte do horário)
        Map<LocalDate, List<LocalDateTime>> groupedByDate = dateTimes.stream()
                .collect(Collectors.groupingBy(LocalDateTime::toLocalDate));

        // Converter para uma lista de DataHourDTO
        return groupedByDate.entrySet().stream()
                .map(entry -> {
                    // Extrai os horários (como HH:mm) para cada grupo de data
                    List<String> hours = entry.getValue().stream()
                            .map(dt -> dt.toLocalTime().toString()) // formato "HH:mm:ss"
                            .map(time -> time.substring(0, 5))       // pega só "HH:mm"
                            .collect(Collectors.toList());

                    // Retorna o DataHourDTO com a data e seus respectivos horários
                    return new DataHourDTO(entry.getKey().atStartOfDay(), hours);
                })
                .collect(Collectors.toList());
    }

    private List<LocalDateTime> toLocalDateTime(List<DataHourDTO> dataHourDTOList) {
        if (dataHourDTOList == null || dataHourDTOList.isEmpty()) {
            return List.of(); // Retorna uma lista vazia se a entrada for nula ou vazia
        }

        // Para cada DataHourDTO, cria um LocalDateTime combinando o dia com as horas
        return dataHourDTOList.stream()
                .flatMap(dataHourDTO ->
                        dataHourDTO.hours().stream()
                                .map(hour -> {
                                    // Converte a hora para um LocalTime
                                    String[] timeParts = hour.split(":");
                                    int hourInt = Integer.parseInt(timeParts[0]);
                                    int minuteInt = Integer.parseInt(timeParts[1]);

                                    // Combina a data (com o início do dia) com a hora extraída de 'hours'
                                    return dataHourDTO.day().toLocalDate().atTime(hourInt, minuteInt);
                                })
                )
                .collect(Collectors.toList());
    }


    public ResponseEntity<Boolean> deleteDateHourAgendamento(DataHourDTO dataHourDTO){
        UUID uuid = securityUtils.getIdUserByFilterSecurity();
        Usuario findUsuario = userService.findUserById(uuid);

        if(findUsuario != null){
            Professional findProfessional = professionalService.findProfessionalByUser(findUsuario);
            if(findProfessional != null){
                List<DataHourDTO> dataHourDTOList = new ArrayList<>();
                dataHourDTOList.add(dataHourDTO);
                List<LocalDateTime> localDateTimes = toLocalDateTime(dataHourDTOList);

                if(!localDateTimes.isEmpty()){
                    DateHourAgendamento dateHourAgendamento = dateHourAgendamentoRepository
                            .findByDayHourAndProfessional(localDateTimes.getFirst(), findProfessional);
                    dateHourAgendamentoRepository.delete(dateHourAgendamento);
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
                }

                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(true);
            }
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
    }

}
