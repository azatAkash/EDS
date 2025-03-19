package com.student.edsbackend.web.service.implementations;

import com.student.edsbackend.dal.token.Token;
import com.student.edsbackend.dal.token.TokenDTO;
import com.student.edsbackend.dal.token.TokenRepository;
import com.student.edsbackend.web.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final TokenRepository tokenRepository;
    @Override
    public Optional<Token> findToken(Integer id) {
        return Optional.empty();
    }

    @Override
    public void updateToken(TokenDTO dto) {
        //TODO
    }

    @Override
    public void deleteToken(Integer id) {
        //TODO
    }
}
