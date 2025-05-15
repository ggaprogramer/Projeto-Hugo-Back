package projeto.hugo.terapia.agendamentos.enumeracoes;

public enum StatusSession {
    PROCESSING("PROCESSANDO"),
    APPROVED("APROVADO"),
    FINISHED("FINALIZADO"),
    CANCELLED("CANCELADO");

    private String status;

    StatusSession(String status) {}
}
