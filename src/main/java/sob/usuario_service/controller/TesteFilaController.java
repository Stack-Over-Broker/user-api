package sob.usuario_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sob.usuario_service.service.UsuarioProducer;

@RestController
@RequestMapping("/fila")
public class TesteFilaController {

    private final UsuarioProducer usuarioProducer;

    public TesteFilaController(UsuarioProducer usuarioProducer) {
        this.usuarioProducer = usuarioProducer;
    }

    @PostMapping
    public ResponseEntity<String> enviar(@RequestBody String json) {
        usuarioProducer.enviarParaFila(json);
        return ResponseEntity.ok("Enviado para a fila com sucesso!");
    }
}

