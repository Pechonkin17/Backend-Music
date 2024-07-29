package kn314.register.validation;

import org.springframework.stereotype.Service;

import java.util.function.Predicate;
import java.util.regex.Pattern;

@Service
public class PasswordValidator implements Predicate<String> {
    private static final String PASSWORD_REGEX =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";

    private final Pattern pattern = Pattern.compile(PASSWORD_REGEX);

    @Override
    public boolean test(String password) {
        return pattern.matcher(password).matches();
    }
}
