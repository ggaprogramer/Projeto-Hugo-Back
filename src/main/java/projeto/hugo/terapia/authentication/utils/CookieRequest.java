package projeto.hugo.terapia.authentication.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import projeto.hugo.terapia.authentication.security.TokenService;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CookieRequest {

    private final TokenService tokenService;

    public UUID getTokenByRequest(String name, HttpServletRequest request){
        if(name != null){
            name = "auth-token";
        }
        String uuidString = this.getTokenHeaderAuthorization(request);



        return UUID.fromString(uuidString);
    }

    public String getCookie(String name, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public String getTokenHeaderAuthorization(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");
        if(authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }

}
