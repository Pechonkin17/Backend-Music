package kn314.register.registration;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode

public class RegistrationRequest {
    final String email;
    final String username;
    final String password;
    LocalDateTime registrationDateTime;
}
