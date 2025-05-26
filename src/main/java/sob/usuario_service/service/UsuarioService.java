package sob.usuario_service.service;

import com.sob.CoreApi.cache.CacheService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sob.usuario_service.dto.UsuarioDTO;
import sob.usuario_service.model.Usuario;
import sob.usuario_service.repository.UsuarioRepository;

import java.util.Optional;

@Data
@AllArgsConstructor
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final CacheService<UsuarioDTO> cacheService;

    public UsuarioDTO buscarUsuarioPorEmail(String email) {
        System.out.println(("[SERVICE] Buscando usuário com e-mail: " + email));
        return cacheService.get(email).orElseThrow(() -> {
            System.out.printf("[SERVICE] Usuário com e-mail: %s não encontrado", email);
            return new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado");
        });
    }

    public void criarUsuario(UsuarioDTO dto) {
        Optional<UsuarioDTO> cacheado = cacheService.get(dto.getEmail());
        if (cacheado.isPresent()) {
            System.out.println("Usuário já cadastrado (cache): " + dto.getEmail());
            return;
        }

        Optional<Usuario> existente = usuarioRepository.findByEmail(dto.getEmail());
        if (existente.isPresent()) {
            System.out.println("Usuário com email " + dto.getEmail() + "já cadastrado");
            return;
        }

        Usuario usuario = UsuarioDTO.toEntity(dto);
        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        cacheService.put(usuarioSalvo.getEmail(), UsuarioDTO.fromModel(usuarioSalvo));
    }

    public void atualizarUsuario(ObjectId usuarioId, UsuarioDTO dto) {
        Optional<Usuario> existente = usuarioRepository.findByEmail(dto.getEmail());
        if (existente.isPresent() && !existente.get().getId().equals(usuarioId.toHexString())) {
            throw new RuntimeException("Email já cadastrado por outro usuário.");
        }

        Usuario usuarioExistente = usuarioRepository.findById(usuarioId.toHexString())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com id: " + usuarioId));
        usuarioExistente.setNome(dto.getNome());
        usuarioExistente.setEmail(dto.getEmail());
        usuarioExistente.setPerfilInvestidor(dto.getPerfilInvestidor());

        Usuario atualizado = usuarioRepository.save(usuarioExistente);
        cacheService.put(atualizado.getEmail(), UsuarioDTO.fromModel(atualizado));
    }

    public void deletarUsuario(String email) {
        usuarioRepository.deleteByEmail(email);
        cacheService.evict(email);
    }
}

