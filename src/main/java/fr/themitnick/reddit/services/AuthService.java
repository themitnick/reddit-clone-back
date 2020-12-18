package fr.themitnick.reddit.services;

import fr.themitnick.reddit.dto.RegisterRequestDTO;
import fr.themitnick.reddit.exceptions.SpringRedditException;
import fr.themitnick.reddit.models.NotificationEmail;
import fr.themitnick.reddit.models.User;
import fr.themitnick.reddit.models.VerificationToken;
import fr.themitnick.reddit.repositories.UserRepository;
import fr.themitnick.reddit.repositories.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;
    @Autowired
    private MailService mailService;

    public void signup(RegisterRequestDTO registerRequestDTO){
        User user = new User();
        user.setUsername(registerRequestDTO.getUsername());
        user.setEmail(registerRequestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
        user.setCreatedDate(Instant.now());
        user.setEnabled(false);

        userRepository.save(user);

        String token = verificationTokenRepository(user);
        mailService.sendMail(new NotificationEmail("Please active your account please", user.getEmail(),
                "Thank you for your signing up to reddit, " +
                "please click on the below url to activate your account : " +
                "http://localhost:8080/api/auth/verifyAccount/" + token));
    }

    private String verificationTokenRepository(User user) {

        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);
        return token;

    }

    public void verifyAccount(String token){
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        verificationToken.orElseThrow(()-> new SpringRedditException("Token Invalid"));
        fetchUserAndEnable(verificationToken.get());
    }

    @Transactional
    public void fetchUserAndEnable(VerificationToken verificationToken){
        String username = verificationToken.getUser().getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new SpringRedditException("User not found with username " + username));
        user.setEnabled(true);
        userRepository.save(user);
    }
}
