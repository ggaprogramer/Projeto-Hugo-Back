package projeto.hugo.terapia.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projeto.hugo.terapia.payment.model.Payment;

import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {



}
