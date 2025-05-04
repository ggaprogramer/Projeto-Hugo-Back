package projeto.hugo.terapia.profile.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table
@Getter
@Setter
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

    @Override
    public String toString() {
        return this.id.toString();
    }

}
