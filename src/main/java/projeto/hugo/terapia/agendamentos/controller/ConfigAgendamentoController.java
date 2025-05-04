package projeto.hugo.terapia.agendamentos.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto.hugo.terapia.agendamentos.dto.ConfigAgendamentoDTO;
import projeto.hugo.terapia.agendamentos.dto.DataHourDTO;
import projeto.hugo.terapia.agendamentos.model.DateHourAgendamento;
import projeto.hugo.terapia.agendamentos.service.ConfigAgendamentoService;

import java.util.List;

@RestController
@RequestMapping("/config-agendamento")
@RequiredArgsConstructor
public class ConfigAgendamentoController {

    private final ConfigAgendamentoService configAgendamentoService;

    @PostMapping("create-config")
    public ResponseEntity<Boolean> cadastrarConfigAgendamento(@RequestBody ConfigAgendamentoDTO configAgendamentoDTO) {
        return configAgendamentoService.cadastrarConfigAgendamento(configAgendamentoDTO);
    }

    @PostMapping("create-hour-day-agendamento")
    public ResponseEntity<Boolean> cadastrarDateHourAgendamento(@RequestBody DataHourDTO dataHourDTO) {
        return configAgendamentoService.cadastrarDateHourAgendamento(dataHourDTO);
    }

    @GetMapping("get-agendamento")
    public ResponseEntity<List<DataHourDTO>> getDateHourAgendamento() {
        return configAgendamentoService.getDateHourAgendamento();
    }

    @DeleteMapping("delete-hour-day-agendamento")
    public ResponseEntity<Boolean> deleteDateHourAgendamento(@RequestBody DataHourDTO dataHourDTO) {
        return configAgendamentoService.deleteDateHourAgendamento(dataHourDTO);
    }

}
