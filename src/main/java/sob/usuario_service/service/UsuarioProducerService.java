package sob.usuario_service.service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.google.gson.Gson;
import lombok.Data;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sob.usuario_service.dto.UsuarioDTO;

@Service
public class UsuarioProducerService {
    private final AmazonSQS sqsClient;

    @Value("${aws.sqs.queue-url}")
    private String queueUrl;

    public UsuarioProducerService(AmazonSQS sqsClient) {
        this.sqsClient = sqsClient;
    }

    public void enviarParaFila(UsuarioDTO usuarioDTO){
        String payload = new Gson().toJson(usuarioDTO);
        SendMessageRequest request = new SendMessageRequest()
                .withQueueUrl(queueUrl)
                .withMessageBody(payload);
        sqsClient.sendMessage(request);
    }
}
