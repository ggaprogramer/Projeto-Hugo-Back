package projeto.hugo.terapia.profile.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import projeto.hugo.terapia.profile.dto.ProfileInfo;
import projeto.hugo.terapia.profile.dto.ProfileUpdateDTO;
import projeto.hugo.terapia.profile.dto.ResponseUpdateDTO;
import projeto.hugo.terapia.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @PreAuthorize("hasAnyRole('PROFILE')")
    @GetMapping
    public ResponseEntity<ProfileInfo> getInfoProfile(){
        return profileService.getInfoProfile();
    }

    @PreAuthorize("hasAnyRole('PROFILE')")
    @PutMapping("/update")
    public ResponseEntity<ResponseUpdateDTO> updateProfile(@RequestBody ProfileUpdateDTO profileUpdateDTO){
        return profileService.updateProfile(profileUpdateDTO);
    }
}
