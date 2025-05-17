package projeto.hugo.terapia.agendamentos.enumeracoes;

public enum StatusSession {
    TODOS("TODOS"),
    PROCESSING("PROCESSANDO"),
    APPROVED("APROVADO"),
    FINISHED("FINALIZADO"),
    CANCELED("CANCELADO");

    private String status;

    StatusSession(String status) {}
}
