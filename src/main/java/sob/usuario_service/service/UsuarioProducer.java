package sob.usuario_service.service;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UsuarioProducer {

    @Value("${aws.sqs.queue-url}")
    private String queueUrl;

    private final AmazonSQSAsync amazonSQSAsync;

    public UsuarioProducer(AmazonSQSAsync amazonSQSAsync) {
        this.amazonSQSAsync = amazonSQSAsync;
    }

    public void enviarParaFila(String payloadJson) {
        amazonSQSAsync.sendMessage(queueUrl, payloadJson);
    }
}

