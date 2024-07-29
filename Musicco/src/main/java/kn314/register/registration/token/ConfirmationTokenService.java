package kn314.register.registration.token;

import kn314.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {
    private final ConfirmationTokenRepository confirmationTokenRepository;

    public void saveConfirmationToken(ConfirmationToken confirmationToken){
        confirmationTokenRepository.save(confirmationToken);
    }

    public Optional<ConfirmationToken> getConfirmationToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    public int setConfirmedAt(String token) {
        return confirmationTokenRepository.updateConfirmedAt(
                token, LocalDateTime.now());
    }

    public ConfirmationToken findConfirmationTokenByUserId(long userId){
        return confirmationTokenRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new ConfirmationTokenNotFoundException("Token by user id: " + userId + "not found"));
    }

    @Transactional
    public void deleteTokensByUser(User user){
        confirmationTokenRepository.deleteByUser(user);
    }
}

