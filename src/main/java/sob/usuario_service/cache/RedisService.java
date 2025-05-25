package sob.usuario_service.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import sob.usuario_service.dto.UsuarioDTO;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void cachearUsuario(UsuarioDTO usuario){
        try {
            String json = objectMapper.writeValueAsString(usuario);
            redisTemplate.opsForValue().set("usuario: " + usuario.getEmail(), json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public UsuarioDTO buscarUsuarioCacheado(String email){
        try {
            String json = redisTemplate.opsForValue().get("usuario:" + email);
            if (json != null){
                return objectMapper.readValue(json, UsuarioDTO.class);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
