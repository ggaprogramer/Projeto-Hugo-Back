package projeto.hugo.terapia.profile.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import projeto.hugo.terapia.profile.dto.ProfileInfo;
import projeto.hugo.terapia.profile.dto.ProfileUpdateDTO;
import projeto.hugo.terapia.profile.dto.ResponseUpdateDTO;
import projeto.hugo.terapia.profile.model.Profile;
import projeto.hugo.terapia.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping("/save")
    public void saveProfile(@RequestBody Profile profile){
        profileService.saveProfile(profile);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping
    public ResponseEntity<ProfileInfo> getInfoProfile(){
        return profileService.getInfoProfile();
    }

    @PutMapping("/update/{uuid}")
    public ResponseEntity<ResponseUpdateDTO> updateProfile(@RequestBody ProfileUpdateDTO profileUpdateDTO){
        return profileService.updateProfile(profileUpdateDTO);
    }
}
