package sob.usuario_service.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import sob.usuario_service.cache.RedisService;
import sob.usuario_service.dto.UsuarioDTO;
import sob.usuario_service.model.Usuario;
import sob.usuario_service.repository.UsuarioRepository;

import java.time.LocalDate;
import java.util.Optional;

@Data
@AllArgsConstructor
@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final RedisService redisService;

    public UsuarioDTO buscarUsuarioPorEmail(String email) {
        UsuarioDTO usuarioCacheado = redisService.buscarUsuarioCacheado(email);
        if (usuarioCacheado != null) {
            System.out.println("Usuario encontrado no cache");
            return usuarioCacheado;
        }

        Optional<Usuario> usuarioBanco = repository.findByEmail(email);
        if (usuarioBanco.isPresent()) {
            UsuarioDTO dto = UsuarioDTO.fromModel(usuarioBanco.get());
            redisService.cachearUsuario(dto);
            return dto;
        }

        throw new IllegalArgumentException("Nenhum usuario encontrado");
    }

    public Usuario criarUsuario(UsuarioDTO dto) {
        Optional<Usuario> email = repository.findByEmail(dto.getEmail());
        if (email.isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado");
        }
        Usuario novoUsuario = Usuario.builder()
                .nome(dto.getNome())
                .email(dto.getEmail())
                .perfilInvestidor(dto.getPerfilInvestidor())
                .criadoEm(LocalDate.now())
                .build();
        return repository.save(novoUsuario);
    }
}

