package projeto.hugo.terapia.agendamentos.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import projeto.hugo.terapia.agendamentos.dto.SessionFilterProfileDTO;
import projeto.hugo.terapia.agendamentos.dto.SessionProfileDTO;
import projeto.hugo.terapia.agendamentos.dto.SessionRequestDTO;
import projeto.hugo.terapia.agendamentos.dto.SessionResponseDTO;
import projeto.hugo.terapia.agendamentos.service.SessionService;

import java.util.List;

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

    @PreAuthorize("hasAnyRole('PROFILE')")
    @PostMapping("profile")
    public Page<SessionProfileDTO> extractSessionsProfile(@RequestBody SessionFilterProfileDTO sessionFilterDTO) {
        return sessionService.extractSessionsProfile(sessionFilterDTO);
    }

}
