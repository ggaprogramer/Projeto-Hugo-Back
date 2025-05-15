package projeto.hugo.terapia.payment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import projeto.hugo.terapia.payment.model.Payment;
import projeto.hugo.terapia.payment.repository.PaymentRepository;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public Payment createPayment(Double amount) {
        Payment payment = new Payment();
        payment.setAmount(amount);
        return paymentRepository.save(payment);
    }

}
