package com.student.edsbackend.web.service;

import com.student.edsbackend.dal.token.Token;
import com.student.edsbackend.dal.token.TokenDTO;
import com.student.edsbackend.dal.user.User;
import com.student.edsbackend.dal.user.UserDTO;

import java.util.Optional;

public interface TokenService {
    Optional<Token> findToken(Integer id);
    void updateToken(TokenDTO dto);
    void deleteToken(Integer id);
}
