package sv.edu.udb.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import sv.edu.udb.repository.UsuarioRepository;
import sv.edu.udb.repository.domain.Usuario;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UsuarioRepository usuarioRepo;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User auth2User = super.loadUser(userRequest);

        String email = auth2User.getAttribute("email");
        String login = auth2User.getAttribute("login");
        String username = (email != null) ? email : login;

        if (username == null) {
            throw new OAuth2AuthenticationException("No se pudo determinar un username único de OAuth2");
        }

        usuarioRepo.findByUsername(username).orElseGet(() -> {
            Usuario newUser = new Usuario();
            newUser.setUsername(username);
            newUser.setPassword("");
            return usuarioRepo.save(newUser);
        });

        return auth2User;
    }
}
