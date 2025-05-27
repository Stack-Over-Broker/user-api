package sob.usuario_service.dto;

import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class UsuarioQueueMessage {
    private String acao;
    private String usuarioId;
    private UsuarioDTO dados;
    private ObjectId trackingId;
}