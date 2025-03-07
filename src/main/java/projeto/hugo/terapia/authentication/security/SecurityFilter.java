package projeto.hugo.terapia.authentication.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import projeto.hugo.terapia.authentication.enumeracoes.RolesUsers;
import projeto.hugo.terapia.authentication.model.Usuario;
import projeto.hugo.terapia.authentication.repository.UserRepository;
import projeto.hugo.terapia.authentication.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.management.relation.Role;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String token = this.recoverToken(request);
        String idUser = tokenService.validateToken(token);

        if(idUser != null){
            Optional<Usuario> usuario = userRepository.findById(UUID.fromString(idUser));

            if(usuario.isPresent()){
                List<RolesUsers> roles = usuario.get().getRoles();
                List<SimpleGrantedAuthority> authorities = roles
                        .stream()
                        .map(role -> new SimpleGrantedAuthority(role.getDescricao()))
                        .collect(Collectors.toList());

                var authentication = new UsernamePasswordAuthenticationToken(usuario.get(), null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else{
                throw new RuntimeException("User not found");
            }
        }
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");
        if(authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }

}
