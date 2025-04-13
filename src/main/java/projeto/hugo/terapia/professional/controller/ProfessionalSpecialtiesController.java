package projeto.hugo.terapia.professional.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import projeto.hugo.terapia.professional.dto.CreateSpecialtiesDTO;
import projeto.hugo.terapia.professional.dto.ProfessionalSpecialtyDTO;
import projeto.hugo.terapia.professional.service.ProfessionalSpecialtiesService;

import java.util.List;

@RestController
@RequestMapping("/professional-specialties")
@RequiredArgsConstructor
public class ProfessionalSpecialtiesController {

    private final ProfessionalSpecialtiesService professionalSpecialtiesService;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/create")
    public List<ProfessionalSpecialtyDTO> createProfessionalSpecialties(
            @RequestBody List<CreateSpecialtiesDTO> createSpecialityDTO){
        return professionalSpecialtiesService.createProfessionalSpecialties(createSpecialityDTO);
    }

    @GetMapping
    public List<ProfessionalSpecialtyDTO> extractProfessionalSpecialties(){
        return professionalSpecialtiesService.extractProfessionalSpecialties();
    }
    
}
