package projeto.hugo.terapia.profile.service;

import projeto.hugo.terapia.authentication.model.Usuario;
import projeto.hugo.terapia.profile.model.Profile;
import projeto.hugo.terapia.profile.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepositoryepository;

    public void saveProfile(Profile profile) {
        profileRepositoryepository.save(profile);
    }

    public Profile findProfileByUser(Usuario usuario){
        return profileRepositoryepository.findByUser(usuario);
    }

}
