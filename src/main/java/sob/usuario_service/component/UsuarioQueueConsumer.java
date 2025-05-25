package sob.usuario_service.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.Data;
import org.springframework.stereotype.Component;
import sob.usuario_service.dto.UsuarioDTO;
import sob.usuario_service.repository.UsuarioRepository;
import sob.usuario_service.service.UsuarioService;

@Component
@Data
public class UsuarioQueueConsumer {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @SqsListener("${aws.sqs.queue-name}")
    public void listen(String message) throws JsonProcessingException {
        try {
            System.out.println("Mensagem recebida da fila " + message);
            UsuarioDTO usuario = objectMapper.readValue(message, UsuarioDTO.class);

            System.out.println("Cadastrando usuario " + usuario);
            usuarioService.criarUsuario(usuario);
        } catch (JsonProcessingException error) {
            error.printStackTrace();
        }
    }
}
