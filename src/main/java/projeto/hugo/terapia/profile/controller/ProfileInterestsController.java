package projeto.hugo.terapia.profile.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import projeto.hugo.terapia.profile.dto.ProfileInterestsDTO;
import projeto.hugo.terapia.profile.model.ProfileInterests;
import projeto.hugo.terapia.profile.service.ProfileInterestsService;

import java.util.List;

@RestController
@RequestMapping("/profile-interests")
@RequiredArgsConstructor
public class ProfileInterestsController {

    private final ProfileInterestsService profileInterestsService;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/create")
    public List<ProfileInterestsDTO> createProfileInterests(@RequestBody List<ProfileInterestsDTO> createProfileInterestsDTO){
        return profileInterestsService.createProfileInterests(createProfileInterestsDTO);
    }

    @GetMapping
    public List<ProfileInterestsDTO> extractProfileInterests(){
        return profileInterestsService.extractProfileInterests();
    }
}
