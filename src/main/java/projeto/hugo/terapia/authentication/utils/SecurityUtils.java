package projeto.hugo.terapia.authentication.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import projeto.hugo.terapia.authentication.model.Usuario;

import java.util.UUID;

@Component
public class SecurityUtils {

    public UUID getIdUserByFilterSecurity(){
        UUID uuid = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Usuario) {
            Usuario user = (Usuario) authentication.getPrincipal();
            uuid = user.getId();
        }
        return uuid;
    }

}
