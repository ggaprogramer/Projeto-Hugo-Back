package projeto.hugo.terapia.profile.repository;

import projeto.hugo.terapia.profile.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ProfileRepository extends JpaRepository<Profile, UUID> {
}
