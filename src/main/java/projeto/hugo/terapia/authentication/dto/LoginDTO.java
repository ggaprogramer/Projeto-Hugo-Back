package projeto.hugo.terapia.authentication.dto;

public record LoginDTO (String email, String password, Boolean lembrarSenha) {
    public LoginDTO(String email, String password, Boolean lembrarSenha){
        this.email = email;
        this.password = password;
        this.lembrarSenha = lembrarSenha != null && lembrarSenha;
    }
}
