package projeto.hugo.terapia.profile.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projeto.hugo.terapia.profile.model.ProfileInterests;

import java.util.UUID;

public interface ProfileInterestsRepository extends JpaRepository<ProfileInterests, UUID> {

    public ProfileInterests findByValue(String value);

}
