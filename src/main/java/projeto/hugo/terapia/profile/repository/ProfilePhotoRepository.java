package projeto.hugo.terapia.profile.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projeto.hugo.terapia.profile.model.ProfileInterests;
import projeto.hugo.terapia.profile.model.ProfilePhoto;

import java.util.UUID;

public interface ProfilePhotoRepository extends JpaRepository<ProfilePhoto, UUID> {

}
