package it.be.fido.admin.auth.controller;

import it.be.fido.admin.auth.dto.RoleDto;
import it.be.fido.admin.auth.dto.UserDto;
import it.be.fido.admin.auth.mapper.AuthMapper;
import it.be.fido.admin.auth.payload.request.LoginRequest;
import it.be.fido.admin.auth.payload.request.SignupRequest;
import it.be.fido.admin.auth.payload.response.JwtResponse;
import it.be.fido.admin.common.payload.response.MessageResponse;
import it.be.fido.admin.enumerations.ERole;
import it.be.fido.admin.security.jwt.JwtUtils;
import it.be.fido.admin.security.services.UserDetailsImpl;
import it.be.fido.admin.services.RoleService;
import it.be.fido.admin.services.UserService;
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
    RoleService roleService;

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    AuthMapper authMapper;

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
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        // rotta usata per la registrazione di un utente
        //vengono effettuati controlli sull'esistenza di username e email e poi dell'esistenza o meno del ruolo.
        //viene ritornato un semplice messaggio di avvenuta registrazione dello user
        if (userService.isExistentUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!", HttpStatus.BAD_REQUEST.value()));
        }

        if (userService.isExistentEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!", HttpStatus.BAD_REQUEST.value()));
        }

        Set<RoleDto> roleDtos = getRoles(signUpRequest);
        // Create new user's account
        UserDto userDto = authMapper.createUserDto(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()),
                roleDtos);

        userService.saveUser(userDto);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!", HttpStatus.OK.value()));
    }

    private Set<RoleDto> getRoles(SignupRequest signUpRequest) {
        Set<String> strRoles = signUpRequest.getRole();
        if (!strRoles.contains("admin") && !strRoles.contains("employee") && !strRoles.contains("user")) {
            throw new IllegalArgumentException("Role must be admin, employee or user.");
        }
        Set<RoleDto> roleDtos = new HashSet<>();

        strRoles.forEach(role -> {
            switch (role) {
                case "admin":
                    RoleDto adminRole = roleService.getRoleByName(ERole.ROLE_ADMIN);
                    roleDtos.add(adminRole);

                    break;
                case "employee":
                    RoleDto employeeRole = roleService.getRoleByName(ERole.ROLE_EMPLOYEE);
                    roleDtos.add(employeeRole);

                    break;
                case "user":
                    RoleDto userRole = roleService.getRoleByName(ERole.ROLE_USER);
                    roleDtos.add(userRole);

                    break;
            }
        });
        return roleDtos;
    }
}
