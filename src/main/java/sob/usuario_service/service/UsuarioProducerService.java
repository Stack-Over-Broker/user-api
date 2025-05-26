package sob.usuario_service.service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.google.gson.Gson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sob.usuario_service.component.AcoesFila;
import sob.usuario_service.dto.UsuarioDTO;

import java.util.HashMap;
import java.util.Map;

@Service
public class UsuarioProducerService {
    private final AmazonSQS sqsClient;

    @Value("${aws.sqs.queue-url}")
    private String queueUrl;

    public UsuarioProducerService(AmazonSQS sqsClient) {
        this.sqsClient = sqsClient;
    }

    public void filaCadastroUsuarios(UsuarioDTO usuarioDTO){
        Map<String, Object> payload = new HashMap<>();
        payload.put("acao", AcoesFila.CADASTRAR_USUARIO);
        payload.put("dados", usuarioDTO);

        String json = new Gson().toJson(payload);
        SendMessageRequest request = new SendMessageRequest()
                .withQueueUrl(queueUrl)
                .withMessageBody(json);
        sqsClient.sendMessage(request);
    }

    public UsuarioDTO filaAtualizacoesUsuario(ObjectId usuarioId, UsuarioDTO usuarioDTO){
        Map<String, Object> payload = new HashMap<>();
        payload.put("acao", AcoesFila.ATUALIZAR_USUARIO);
        payload.put("usuarioId", usuarioId.toHexString());
        payload.put("dados", usuarioDTO);

        String json = new Gson().toJson(payload);
        SendMessageRequest request = new SendMessageRequest()
                .withQueueUrl(queueUrl)
                .withMessageBody(json);
        sqsClient.sendMessage(request);
        return usuarioDTO;
    }
}
