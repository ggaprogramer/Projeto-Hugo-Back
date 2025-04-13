package projeto.hugo.terapia.professional.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import projeto.hugo.terapia.professional.dto.CreateProfessionalInterestsDTO;
import projeto.hugo.terapia.professional.dto.ProfessionalInterestsDTO;
import projeto.hugo.terapia.professional.model.ProfessionalInterests;
import projeto.hugo.terapia.professional.service.ProfessionalInterestsService;

import java.util.List;

@RestController
@RequestMapping("/professional-interests")
@RequiredArgsConstructor
public class ProfessionalInterestsController {

    private final ProfessionalInterestsService professionalInterestsService;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/create")
    public List<ProfessionalInterestsDTO> createProfessionalInterests(
            @RequestBody List<CreateProfessionalInterestsDTO> createProfessionalInterestsDTOS){
        return professionalInterestsService.createProfessionalInterests(createProfessionalInterestsDTOS);
    }

    @GetMapping
    public List<ProfessionalInterestsDTO> extractProfessionalInterests(){
        return professionalInterestsService.extractProfessionalInterests();
    }
}