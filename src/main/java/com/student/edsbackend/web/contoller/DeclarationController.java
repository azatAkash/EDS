// package com.student.edsbackend.web.contoller;

// import com.student.edsbackend.dal.declaration.DeclarationDTO;
// import com.student.edsbackend.web.service.DeclarationService;
// import lombok.RequiredArgsConstructor;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import java.util.List;

// @RestController  // using @RestController to automatically serialize responses to JSON
// @RequiredArgsConstructor
// @RequestMapping("/api/v1/declarations")
// public class DeclarationController {

//     private final DeclarationService declarationService;

//     // Endpoint for a regular user to get their declarations.
//     // In a production scenario, the userId would be obtained from the JWT.
//     @GetMapping("/user")
//     public ResponseEntity<DeclarationDTO> findDeclarationsForUser() {
//         DeclarationDTO declaration = declarationService.findDeclarationForUser();
//         return ResponseEntity.ok(declaration);
//     }

//     // Endpoint for an admin to get all declarations.
//     @GetMapping("/admin")
//     public ResponseEntity<List<DeclarationDTO>> findAllDeclarationsForAdmin() {
//         List<DeclarationDTO> declarations = declarationService.findAllForAdmin();
//         return ResponseEntity.ok(declarations);
//     }

//     @PostMapping("/create")
//     public ResponseEntity<String> createDeclaration(@RequestBody DeclarationDTO dto) {
//         declarationService.createDeclaration(dto);
//         return ResponseEntity.ok("Declaration was successfully created");
//     }

//     @PutMapping("/update")
//     public ResponseEntity<String> updateDeclaration(@RequestBody DeclarationDTO dto) {
//         declarationService.updateDeclaration(dto);
//         return ResponseEntity.ok("Declaration was successfully updated");
//     }

//     @DeleteMapping("/delete")
//     public ResponseEntity<String> deleteDeclaration(@RequestParam Integer id) {
//         declarationService.deleteDeclaration(id);
//         return ResponseEntity.ok("Declaration was successfully deleted");
//     }
// }
