package kn314.service;

import kn314.exception.UserNotFoundException;
import kn314.model.User;
import kn314.register.registration.token.ConfirmationToken;
import kn314.register.registration.token.ConfirmationTokenService;
import kn314.register.validation.EmailValidator;
import kn314.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder  bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailValidator emailValidator;

    public List<User> findAllUsers(){
        return userRepository.findAll();
    }

    public User updateUser(String email, User user){
//        return userRepository.save(user);

        boolean ifUserExists = userRepository.existsByEmail(email);
        if (!ifUserExists){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Email not valid");
        } else return userRepository.save(user);
    }

    public User findByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User by email " + email + " was not found"));
    }

    public User findByUsername(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UserNotFoundException("User by username " + username + " was not found"));
    }

    public void deleteUserById(long id){
        userRepository.deleteUserById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User by email " + email + "was not found"));
    }

    public String signUpUser(User user){
        String encodedPassword = bCryptPasswordEncoder
                .encode(user.getPassword());

        user.setPassword(encodedPassword);

        userRepository.save(user);

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                user
        );

        confirmationTokenService.saveConfirmationToken(confirmationToken);

        return token;
    }

    public int enableUser(String email) {
        return userRepository.enableUser(email);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public String loginUser(String email, String password) {
        boolean isValidEmail = emailValidator.test(email);
        if (!isValidEmail){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Email not valid");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User by this email: " + email + " does not exist, please sign up new account"
                ));

        ConfirmationToken confirmationToken =
                confirmationTokenService.findConfirmationTokenByUserId(user.getId());

        if (confirmationToken.getConfirmedAt() == null &&
                confirmationToken.getExpiresAt().isAfter(LocalDateTime.now())){

            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Email is not confirmed, please confirm your email");

        } else if (confirmationToken.getConfirmedAt() == null &&
                confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())){

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token expired");
        }

        if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Invalid password");
        }

        LocalDateTime confirmedAt = confirmationToken.getConfirmedAt();
        confirmationTokenService.deleteTokensByUser(user);

        String token = UUID.randomUUID().toString();
        ConfirmationToken authenticationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(240),
                user
        );
        authenticationToken.setConfirmedAt(confirmedAt);

        confirmationTokenService.saveConfirmationToken(authenticationToken);

        return token;
    }
}

