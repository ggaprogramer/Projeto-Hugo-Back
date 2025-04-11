package projeto.hugo.terapia.professional.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import projeto.hugo.terapia.professional.dto.CreateLanguagesDTO;
import projeto.hugo.terapia.professional.dto.ProfessionalLanguageDTO;
import projeto.hugo.terapia.professional.enumeracoes.LevelLanguage;
import projeto.hugo.terapia.professional.service.ProfessionalLanguagesService;

import java.util.List;

@RestController
@RequestMapping("/professional-languages")
@RequiredArgsConstructor
public class ProfessionalLanguagesController {

    private final ProfessionalLanguagesService professionalLanguagesService;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/create")
    public List<ProfessionalLanguageDTO> createProfessionalLanguages(
            @RequestBody List<CreateLanguagesDTO> createLanguageDTO){
        return professionalLanguagesService.createProfessionalLanguages(createLanguageDTO);
    }

    @GetMapping
    public List<ProfessionalLanguageDTO> extractProfessionalLanguages(){
        return professionalLanguagesService.extractProfessionalLanguages();
    }

    @GetMapping("/level")
    public List<LevelLanguage> extractLevelLanguages(){
        return professionalLanguagesService.extractLevelLanguages();
    }

}
