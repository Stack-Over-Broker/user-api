package sob.usuario_service.controller;

import com.sob.core_api.model.ApiResponse;
import com.sob.core_api.service.StatusOperacaoService;
import lombok.Data;
import org.bson.types.ObjectId;
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
    private final StatusOperacaoService statusOperacaoService;

    @PostMapping
    public ResponseEntity<ApiResponse<ObjectId>> criarUsuario(@RequestBody UsuarioDTO usuarioDTO){
        ObjectId trackingId = new ObjectId();
        statusOperacaoService.criarStatus(trackingId, "CADASTRAR_USUARIO");
        usuarioProducerService.filaCadastroUsuarios(usuarioDTO, trackingId);
        return ResponseEntity.accepted().body(
                ApiResponse.ok("Usuário enviado para fila", trackingId)
        );
    }

    @PostMapping("/atualizarUsuario/{id}")
    public ResponseEntity<ApiResponse<ObjectId>> atualizarUsuario(
            @PathVariable("id") ObjectId usuarioId,
            @RequestBody UsuarioDTO usuarioDTO
    ){
        ObjectId trackingId = new ObjectId();
        statusOperacaoService.criarStatus(trackingId, "ATUALIZAR_USUARIO");
        usuarioProducerService.filaAtualizacoesUsuario(usuarioId, usuarioDTO, trackingId);
        return ResponseEntity.accepted().body(
                ApiResponse.ok("Usuário enviado para fila", trackingId)
        );
    }

    @GetMapping("/{email}")
    public ResponseEntity<UsuarioDTO> buscarUsuario(@PathVariable String email) {
        UsuarioDTO usuario = usuarioService.buscarUsuarioPorEmail(email);
        return usuario != null ? ResponseEntity.ok(usuario) : ResponseEntity.notFound().build();
    }
}

