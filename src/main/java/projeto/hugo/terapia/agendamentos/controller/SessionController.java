package projeto.hugo.terapia.agendamentos.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import projeto.hugo.terapia.agendamentos.dto.SessionRequestDTO;
import projeto.hugo.terapia.agendamentos.dto.SessionResponseDTO;
import projeto.hugo.terapia.agendamentos.service.SessionService;

@RestController
@RequestMapping("/session")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;

    @PreAuthorize("hasAnyRole('PROFILE')")
    @PostMapping("create")
    public ResponseEntity<SessionResponseDTO> createSession(@RequestBody SessionRequestDTO sessionRequestDTO) {
        return sessionService.createSession(sessionRequestDTO);
    }

}
