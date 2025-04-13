package projeto.hugo.terapia.professional.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import projeto.hugo.terapia.professional.dto.CreateApproachesDTO;
import projeto.hugo.terapia.professional.dto.ProfessionalApproachDTO;
import projeto.hugo.terapia.professional.service.ProfessionalApproachesService;

import java.util.List;

@RestController
@RequestMapping("/professional-approaches")
@RequiredArgsConstructor
public class ProfessionalApproachesController {

    private final ProfessionalApproachesService professionalApproachesService;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/create")
    public List<ProfessionalApproachDTO> createProfessionalApproaches(
            @RequestBody List<CreateApproachesDTO> createApproachesDTO){
        return professionalApproachesService.createProfessionalApproaches(createApproachesDTO);
    }

    @GetMapping
    public List<ProfessionalApproachDTO> extractProfessionalApproaches(){
        return professionalApproachesService.extractProfessionalApproaches();
    }
}
