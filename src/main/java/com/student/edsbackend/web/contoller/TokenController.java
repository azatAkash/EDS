// package com.student.edsbackend.web.contoller;

// import com.student.edsbackend.dal.declaration.DeclarationDTO;
// import com.student.edsbackend.dal.token.Token;
// import com.student.edsbackend.dal.token.TokenDTO;
// import com.student.edsbackend.web.service.DeclarationService;
// import com.student.edsbackend.web.service.TokenService;
// import lombok.RequiredArgsConstructor;
// import org.springframework.http.ResponseEntity;
// import org.springframework.stereotype.Controller;
// import org.springframework.web.bind.annotation.*;

// @Controller
// @RequiredArgsConstructor
// @RequestMapping("/api/v1/tokens")
// public class TokenController {
//     private final TokenService tokenService;

//     @GetMapping("/get")
//     public ResponseEntity<Token> findToken(Integer id) {
//         return ResponseEntity.ofNullable(tokenService.findToken(id).get());
//     }

// //    @PostMapping("/create")
// //    public void createToken(TokenDTO dto) {
// //
// //    }

//     @PutMapping("/update")
//     public ResponseEntity<String> updateToken(@RequestBody TokenDTO dto) {
//         tokenService.updateToken(dto);

//         return ResponseEntity.ok("Token was successfully updated");
//     }
//     @DeleteMapping("/delete")
//     public ResponseEntity<String> deleteToken(@RequestParam(name = "id") Integer id) {
//         tokenService.deleteToken(id);

//         return ResponseEntity.ok("Token was successfully deleted");
//     }
// }
