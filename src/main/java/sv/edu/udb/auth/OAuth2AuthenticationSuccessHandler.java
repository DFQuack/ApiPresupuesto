package sv.edu.udb.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import sv.edu.udb.repository.UsuarioRepository;
import sv.edu.udb.repository.domain.Usuario;

import java.io.IOException;

import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepo;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth) throws IOException {
        OAuth2User auth2User = (OAuth2User) auth.getPrincipal();

        String email = auth2User.getAttribute("email");
        String username = (email != null) ? email : auth2User.getAttribute("login");

        if (username == null) {
            response.sendError(SC_INTERNAL_SERVER_ERROR, "No se pudo obtener el email ni el login");
            return;
        }

        Usuario user = usuarioRepo.findByUsername(username).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        String jwt = jwtService.generateToken(user);

        // Respuesta JSON directa (para clientes REST)
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"token\": \"" + jwt + "\"}");
    }
}
