package projeto.hugo.terapia.professional.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import projeto.hugo.terapia.professional.dto.ProfessionalInfo;
import projeto.hugo.terapia.professional.dto.ProfessionalUpdateDTO;
import projeto.hugo.terapia.professional.service.ProfessionalService;
import projeto.hugo.terapia.profile.dto.ProfileInfo;
import projeto.hugo.terapia.profile.dto.ProfileUpdateDTO;
import projeto.hugo.terapia.profile.dto.ResponseUpdateDTO;
import projeto.hugo.terapia.profile.dto.ResponseUrlPhotoDTO;

import java.util.UUID;

@RestController
@RequestMapping("/professional")
@RequiredArgsConstructor
public class ProfessionalController {

    private final ProfessionalService professionalService;


    @GetMapping
    public ResponseEntity<ProfessionalInfo> getInfoProfessional(){
        return professionalService.getInfoProfessional();
    }

    @PreAuthorize("hasAnyRole('PROFESSIONAL')")
    @PutMapping("/update")
    public ResponseEntity<ResponseUpdateDTO> updateProfessional(@RequestBody ProfessionalUpdateDTO professionalUpdateDTO){
        return professionalService.updateProfessional(professionalUpdateDTO);
    }

    @GetMapping("/photo/{uuid}")
    public ResponseEntity<ResponseUrlPhotoDTO> getUrlPhoto(@PathVariable UUID uuid){
        return professionalService.getUrlPhoto(uuid);
    }
}
