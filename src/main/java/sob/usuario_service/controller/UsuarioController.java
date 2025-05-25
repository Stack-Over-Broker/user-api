package sob.usuario_service.controller;

import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sob.usuario_service.dto.UsuarioDTO;
import sob.usuario_service.service.UsuarioProducerService;
import sob.usuario_service.service.UsuarioService;

@Data
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioProducerService usuarioProducerService;
    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<Void> criarUsuario(@RequestBody UsuarioDTO usuarioDTO){
        usuarioProducerService.enviarParaFila(usuarioDTO);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/{email}")
    public ResponseEntity<UsuarioDTO> buscarUsuario(@PathVariable String email) {
        UsuarioDTO usuario = usuarioService.buscarUsuarioPorEmail(email);
        return usuario != null ? ResponseEntity.ok(usuario) : ResponseEntity.notFound().build();
    }
}

