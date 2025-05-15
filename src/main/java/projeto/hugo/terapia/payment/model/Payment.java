package projeto.hugo.terapia.payment.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import projeto.hugo.terapia.agendamentos.model.Session;
import projeto.hugo.terapia.payment.enumeracoes.StatusPayment;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table
@Getter
@Setter
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(mappedBy = "payment", cascade = CascadeType.ALL)
    private Session session;

    @Column
    private Boolean active;

    @Column
    private Double amount;

    @Column(columnDefinition="varchar")
    @Enumerated(EnumType.STRING)
    private StatusPayment statusPayment;

    @Column(updatable = false)
    private LocalDateTime dateCreated;

    @Column
    private LocalDateTime lastModifiedDate;

    @PrePersist
    private void prePersist(){
        if(this.active == null){
            setActive(false);
        }
        if(this.statusPayment == null){
            setStatusPayment(StatusPayment.PENDING);
        }
        if(this.dateCreated == null){
            setDateCreated(LocalDateTime.now());
        }
        if(this.lastModifiedDate == null){
            setLastModifiedDate(LocalDateTime.now());
        }
    }

    @PreUpdate
    private void preUpdate(){
        setLastModifiedDate(LocalDateTime.now());
    }

    @Override
    public String toString() {
        return id.toString();
    }

}
