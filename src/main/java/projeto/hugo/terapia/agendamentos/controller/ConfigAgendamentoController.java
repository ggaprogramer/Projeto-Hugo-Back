package projeto.hugo.terapia.agendamentos.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import projeto.hugo.terapia.agendamentos.dto.ConfigAgendamentoDTO;
import projeto.hugo.terapia.agendamentos.service.ConfigAgendamentoService;

@RestController
@RequestMapping("/config-agendamento")
@RequiredArgsConstructor
public class ConfigAgendamentoController {

    private final ConfigAgendamentoService configAgendamentoService;

    @PostMapping
    public ResponseEntity<?> cadastrarConfigAgendamento(@RequestBody ConfigAgendamentoDTO configAgendamentoDTO) {
        return configAgendamentoService.cadastrarConfigAgendamento(configAgendamentoDTO);
    }

}
