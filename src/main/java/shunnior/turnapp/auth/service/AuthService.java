package shunnior.turnapp.auth.service;

import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import shunnior.turnapp.auth.controller.AuthRequest;
import shunnior.turnapp.auth.controller.RegisterRequest;
import shunnior.turnapp.auth.controller.TokenResponse;
import shunnior.turnapp.auth.repository.Token;
import shunnior.turnapp.auth.repository.TokenRepository;
import shunnior.turnapp.auth.service.exceptions.UserEntityException;
import shunnior.turnapp.auth.service.exceptions.UserEntityExceptionType;
import shunnior.turnapp.user.User;
import shunnior.turnapp.user.UserRepository;
import shunnior.turnapp.user.roles.Role;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository repository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public TokenResponse register(final RegisterRequest request, boolean isAdmin) {
        if(repository.findByEmail(request.email()).isPresent()){
            throw new UserEntityException(UserEntityExceptionType.USER_ALREADY_EXISTS);
        }

        final User user = User.builder()
                .name(request.name())
                .email(request.email())
                .roles(Set.of(isAdmin?Role.ROLE_ADMIN:Role.ROLE_USER))
                .password(passwordEncoder.encode(request.password()))
                .build();

        final User savedUser = repository.save(user);
        final String jwtToken = jwtService.generateToken(savedUser);
        final String refreshToken = jwtService.generateRefreshToken(savedUser);

        saveUserToken(savedUser, jwtToken);
        return new TokenResponse(jwtToken, refreshToken,savedUser.getName(),savedUser.getEmail());
    }

    public TokenResponse authenticate(final AuthRequest request) {
        final User user = repository.findByEmail(request.email())
                .orElseThrow(()->new UserEntityException(UserEntityExceptionType.USER_NOT_FOUND));
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        final String accessToken = jwtService.generateToken(user);
        final String refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);
        return new TokenResponse(accessToken, refreshToken,user.getName(),user.getEmail());
    }

    private void saveUserToken(User user, String jwtToken) {
        final Token token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(Token.TokenType.BEARER)
                .isExpired(false)
                .isRevoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(final User user) {
        final List<Token> validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (!validUserTokens.isEmpty()) {
            validUserTokens.forEach(token -> {
                token.setIsExpired(true);
                token.setIsRevoked(true);
            });
            tokenRepository.saveAll(validUserTokens);
        }
    }

    public TokenResponse refreshToken(@NotNull final String authentication) {

        if (authentication == null || !authentication.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid auth header");
        }
        final String refreshToken = authentication.substring(7);
        final String userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail == null) {
            return null;
        }

        final User user = this.repository.findByEmail(userEmail).orElseThrow();
        final boolean isTokenValid = jwtService.isTokenValid(refreshToken, user);
        if (!isTokenValid) {
            return null;
        }

        final String accessToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);

        return new TokenResponse(accessToken, refreshToken,user.getName(),user.getEmail());
    }
}