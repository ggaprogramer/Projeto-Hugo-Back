package projeto.hugo.terapia.payment.enumeracoes;

public enum StatusPayment {
    TODOS("TODOS"),
    APPROVED("APROVADO"),
    PENDING("PENDENTE"),
    REJECTED("REJEITADO");

    private String status;

    StatusPayment(String status) {}
}
