package projeto.hugo.terapia.profile.controller;

import org.springframework.http.ResponseEntity;
import projeto.hugo.terapia.authentication.dto.ResponseLoginDTO;
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

    @GetMapping("/{uuid}")
    public ResponseEntity<ProfileInfo> getInfoProfile(@PathVariable UUID uuid){
        return profileService.getInfoProfile(uuid);
    }

    @PutMapping("/update/{uuid}")
    public ResponseEntity<ResponseUpdateDTO> updateProfile(@PathVariable UUID uuid,
                                                           @RequestBody ProfileUpdateDTO profileUpdateDTO){
        return profileService.updateProfile(uuid, profileUpdateDTO);
    }
}
