package it.be.fido.admin.controllers;

import it.be.fido.admin.exceptions.RoleNotExistsException;
import it.be.fido.admin.models.ERole;
import it.be.fido.admin.models.Role;
import it.be.fido.admin.models.User;
import it.be.fido.admin.payload.request.LoginRequest;
import it.be.fido.admin.payload.request.SignupRequest;
import it.be.fido.admin.payload.response.JwtResponse;
import it.be.fido.admin.payload.response.MessageResponse;
import it.be.fido.admin.repository.UserRepository;
import it.be.fido.admin.security.jwt.JwtUtils;
import it.be.fido.admin.security.services.UserDetailsImpl;
import it.be.fido.admin.services.RoleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleService roleService;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        // rotta usata per l'autenticazione di un utente
        // se utente e password corrispondono viene ritornato un token da usare per navigare il sito
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) throws RoleNotExistsException {
        // rotta usata per la registrazione di un utente
        //vengono effettuati controlli sull'esistenza di username e email e poi dell'esistenza o meno del ruolo.
        //viene ritornato un semplice messaggio di avvenuta registrazione dello user
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!", HttpStatus.BAD_REQUEST.value()));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!", HttpStatus.BAD_REQUEST.value()));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<Role> roles = getRoles(signUpRequest);

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!", HttpStatus.OK.value()));
    }

    private Set<Role> getRoles(SignupRequest signUpRequest) throws RoleNotExistsException {
        Set<String> strRoles = signUpRequest.getRole();
        if (!strRoles.contains("admin") && !strRoles.contains("employee") && !strRoles.contains("user")) {
            throw new RoleNotExistsException("Role must be admin, employee or user.");
        }
        Set<Role> roles = new HashSet<>();

        strRoles.forEach(role -> {
            switch (role) {
                case "admin":
                    Role adminRole = roleService.getRoleByName(ERole.ROLE_ADMIN).get();
                    roles.add(adminRole);

                    break;
                case "employee":
                    Role modRole = roleService.getRoleByName(ERole.ROLE_EMPLOYEE).get();
                    roles.add(modRole);

                    break;
                case "user":
                    Role userRole = roleService.getRoleByName(ERole.ROLE_USER).get();
                    roles.add(userRole);

                    break;
            }
        });
        return roles;
    }
}
