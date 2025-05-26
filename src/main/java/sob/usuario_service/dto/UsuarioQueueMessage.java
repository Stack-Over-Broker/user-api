package sob.usuario_service.dto;

import lombok.Data;

@Data
public class UsuarioQueueMessage {
    private String acao;
    private String usuarioId;
    private UsuarioDTO dados;
}