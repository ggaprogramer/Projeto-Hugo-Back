package projeto.hugo.terapia.authentication.controller;

import projeto.hugo.terapia.authentication.model.Usuario;
import projeto.hugo.terapia.authentication.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public List<Usuario> encontrarTodos(){
        return userService.encontrarTodos();
    }

}
