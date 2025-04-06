package com.student.edsbackend.web.service;

import java.util.Optional;

import com.student.edsbackend.features.token.Token;
import com.student.edsbackend.features.token.TokenDTO;
import com.student.edsbackend.features.user.dal.User;
import com.student.edsbackend.features.user.dal.UserDTO;

public interface TokenService {
    Optional<Token> findToken(Integer id);
    void updateToken(TokenDTO dto);
    void deleteToken(Integer id);
}
