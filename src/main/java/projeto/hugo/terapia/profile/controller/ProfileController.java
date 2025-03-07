package projeto.hugo.terapia.profile.controller;

import projeto.hugo.terapia.profile.model.Profile;
import projeto.hugo.terapia.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping("/save")
    public void saveProfile(@RequestBody Profile profile){
        profileService.saveProfile(profile);
    }

}
