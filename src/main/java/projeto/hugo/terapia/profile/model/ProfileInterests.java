package projeto.hugo.terapia.profile.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table
@Data
public class ProfileInterests {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private String value;

    @Column
    private String label;

    @ManyToMany(mappedBy = "interests")
    private List<Profile> profiles = new ArrayList<>();

}
