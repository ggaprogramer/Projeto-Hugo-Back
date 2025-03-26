package projeto.hugo.terapia.authentication.service;

import projeto.hugo.terapia.authentication.dto.LoginDTO;
import projeto.hugo.terapia.authentication.dto.RegistroDTO;
import projeto.hugo.terapia.authentication.model.Usuario;
import projeto.hugo.terapia.authentication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public Boolean findUserByEmail(@PathVariable String email){
        Optional<Usuario> usuario = userRepository.findByEmail(email);
        if(usuario.isPresent()){
            return true;
        }
        return false;
    }

    public Boolean findUserByUsername(@PathVariable String username){
        Optional<Usuario> usuario = userRepository.findByUsername(username);
        if(usuario.isPresent()){
            return true;
        }
        return false;
    }

    public Usuario encontrarPorId(UUID uuid){
        Optional<Usuario> usuario = userRepository.findById(uuid);
        return usuario.orElse(null);
    }
    public Usuario encontrarPorId(String id){
        UUID uuidUser = UUID.fromString(id);
        Optional<Usuario> usuario = userRepository.findById(uuidUser);
        return usuario.orElse(null);
    }

    public List<Usuario> encontrarTodos(){
        return userRepository.findAll();
    }

    public Usuario encontrarPorEmail(String email){
        Optional<Usuario> usuario = userRepository.findByEmail(email);
        return usuario.orElse(null);
    }

    public Usuario encontrarPorUsername(String username){
        Optional<Usuario> usuario = userRepository.findByUsername(username);
        return usuario.orElse(null);
    }

    public Usuario salvarUsuario(RegistroDTO registroDTO){
        Usuario usuario = new Usuario();

        String passwordCriptografada = encoder.encode(registroDTO.password1());
        usuario.setPassword(passwordCriptografada);
        usuario.setEmail(registroDTO.email());
        usuario.setUsername(registroDTO.username());
        usuario.setRoles(registroDTO.roles());

        return userRepository.save(usuario);
    }

    public void updatePasswordUsuario(Usuario usuario, String password1){
        String passwordCriptografada = encoder.encode(password1);
        usuario.setPassword(passwordCriptografada);

        userRepository.save(usuario);
    }

    public Usuario atualizarUsuario(Usuario usuario){
        return userRepository.save(usuario);
    }

    public int calcularIdade(String dataNascimento) {
        // Converter a string de dataNascimento para LocalDate
        LocalDate nascimento = LocalDate.parse(dataNascimento);
        LocalDate hoje = LocalDate.now(); // Data atual

        // Calcular a idade
        int idade = Period.between(nascimento, hoje).getYears();

        // Verifica se a pessoa já fez aniversário esse ano
        if (hoje.getMonthValue() < nascimento.getMonthValue() ||
                (hoje.getMonthValue() == nascimento.getMonthValue() && hoje.getDayOfMonth() < nascimento.getDayOfMonth())) {
            idade--; // Se não, diminui 1 ano
        }

        return idade;
    }

}
