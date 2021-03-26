package yellowpepper.challenge.transfers.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yellowpepper.challenge.transfers.dto.request.AuthenticateRequestDto;
import yellowpepper.challenge.transfers.dto.response.AuthenticateResponseDto;
import yellowpepper.challenge.transfers.util.JwtTokenUtil;

import static io.vavr.API.Try;

@RestController
@RequestMapping("/authenticate")
public class AuthenticationController {

    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationController(@Qualifier("jwtUserDetailService") UserDetailsService userDetailsService,
                                    JwtTokenUtil jwtTokenUtil, AuthenticationManager authenticationManager) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping
    public ResponseEntity<AuthenticateResponseDto> createAuthenticationToken(
            @RequestBody AuthenticateRequestDto authenticateRequestDto) {
        authenticate(authenticateRequestDto.getUsername(), authenticateRequestDto.getPassword());
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticateRequestDto.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new AuthenticateResponseDto(token));
    }

    private void authenticate(String username, String password) {
        Try(()->authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password)))
                .toEither().fold(exception-> null, response-> response);
    }
}
