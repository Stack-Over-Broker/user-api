package sob.usuario_service.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sob.core_api.model.StatusTracking;
import com.sob.core_api.service.StatusOperacaoService;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;
import sob.usuario_service.dto.UsuarioQueueMessage;
import sob.usuario_service.repository.UsuarioRepository;
import sob.usuario_service.service.UsuarioService;

@Component
@Data
public class UsuarioQueueConsumer {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final StatusOperacaoService statusOperacaoService;

    @SqsListener("${aws.sqs.queue-name}")
    public void listen(String message) {
        try {
            UsuarioQueueMessage queueMessage = objectMapper.readValue(message, UsuarioQueueMessage.class);
            System.out.println("Mensagem recebida: " + message);

            switch (queueMessage.getAcao()) {
                case "CADASTRAR_USUARIO":
                    usuarioService.criarUsuario(queueMessage.getDados());
                    if (queueMessage.getTrackingId() != null) {
                        statusOperacaoService.atualizarStatus(queueMessage.getTrackingId(), StatusTracking.SUCCESS, "Usuário cadastrado");
                    }
                    break;
                case "ATUALIZAR_USUARIO":
                    ObjectId usuarioId = new ObjectId(queueMessage.getUsuarioId());
                    boolean usuario = usuarioRepository.existsById(usuarioId);
                    if (!usuario) {
                        System.out.println("Usuário não encontrado: " + usuarioId + ", removendo mensagem da fila");
                        if (queueMessage.getTrackingId() != null) {
                            statusOperacaoService.atualizarStatus(queueMessage.getTrackingId(), StatusTracking.ERROR, "Usuário não encontrado");
                        }
                        return;
                    }
                    usuarioService.atualizarUsuario(usuarioId, queueMessage.getDados());
                    if (queueMessage.getTrackingId() != null) {
                        statusOperacaoService.atualizarStatus(queueMessage.getTrackingId(), StatusTracking.SUCCESS, "Usuário atualizado");
                    }
                    break;
                default:
                    System.out.println("Ação desconhecida: " + queueMessage.getAcao());
            }
        } catch (JsonProcessingException e) {
            System.err.println("Erro ao processar mensagem da fila: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
