package fr.themitnick.reddit.controllers;

import fr.themitnick.reddit.dto.RegisterRequestDTO;
import fr.themitnick.reddit.services.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody RegisterRequestDTO registerRequestDTO){
        authService.signup(registerRequestDTO);
        return new ResponseEntity<>("User Registration Successfull", HttpStatus.OK);
    }

    @GetMapping("/verifyAccount/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token){
        authService.verifyAccount(token);
        return new ResponseEntity<>("Acount Activated Successfuly ", HttpStatus.OK);
    }
}
