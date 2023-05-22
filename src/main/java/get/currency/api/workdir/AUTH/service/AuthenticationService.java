package get.currency.api.workdir.AUTH.service;

import get.currency.api.workdir.AUTH.model.User;
import get.currency.api.workdir.AUTH.repository.UserRepository;
import get.currency.api.workdir.AUTH.security.JwtTokenProvider;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthenticationService(AuthenticationManager authenticationManager, UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public ResponseEntity<?> registerUser(String email, String password, String firstName, String lastName) {
        try {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
            String encryptedPassword = passwordEncoder.encode(password);

            User newUser = new User(email, encryptedPassword, firstName, lastName);
            userRepository.save(newUser);

            return getResponseEntity(email, newUser);

        } catch (DataIntegrityViolationException e) {
            Throwable rootCause = e.getRootCause();
            String errorMessage = "Ошибка при регистрации";

            if (rootCause instanceof ConstraintViolationException) {
                ConstraintViolationException constraintViolationException = (ConstraintViolationException) rootCause;
                errorMessage += ": " + constraintViolationException.getMessage();
            } else {
                errorMessage += ": " + e.getMessage();
            }
            return ResponseEntity.badRequest().body("Пользователь уже зарегистрирован, либо передано некорректное значение полей");
        }
    }

    private ResponseEntity<?> getResponseEntity(String email, User newUser) {
        String token = jwtTokenProvider.createToken(email, newUser.getRole().name());
        Map<Object, Object> response = new HashMap<>();
        response.put("email", email);
        response.put("token", token);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> authenticate(String email, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User doesn't exist"));
            return getResponseEntity(email, user);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>("Неверная комбинация пароля и логина", HttpStatus.FORBIDDEN);
        }
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, response, null);
    }
}