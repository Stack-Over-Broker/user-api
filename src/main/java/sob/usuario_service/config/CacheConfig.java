package sob.usuario_service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sob.core_api.cache.CacheService;
import com.sob.core_api.cache.RedisCacheService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import sob.usuario_service.dto.UsuarioDTO;
import sob.usuario_service.model.Usuario;
import sob.usuario_service.repository.UsuarioRepository;

import java.time.Duration;

@Configuration
public class CacheConfig {

    @Bean
    public CacheService<UsuarioDTO> usuarioCacheService(
            StringRedisTemplate redisTemplate,
            ObjectMapper objectMapper,
            UsuarioRepository usuarioRepository
    ) {
        return new RedisCacheService<>(
                redisTemplate,
                objectMapper,
                key -> {
                    System.out.println("[FALLBACK - DATABASE] Buscando usuário no banco para chave: " + key);
                    Usuario usuario = usuarioRepository.findByEmail(key)
                            .orElse(null);
                    return  usuario != null ? UsuarioDTO.fromModel(usuario) : null;
                },
                UsuarioDTO.class,
                Duration.ofMinutes(10)
        );
    }
}
