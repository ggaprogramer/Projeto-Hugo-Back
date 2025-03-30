package projeto.hugo.terapia.professional.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import projeto.hugo.terapia.professional.service.ProfessionalService;

@RestController
@RequestMapping("/professional")
@RequiredArgsConstructor
public class ProfessionalController {

    private final ProfessionalService professionalService;

}
