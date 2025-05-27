package sob.usuario_service.dto;

import lombok.*;
import sob.usuario_service.model.TipoPerfil;
import sob.usuario_service.model.Usuario;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioDTO {
    private String nome;
    private String email;
    private TipoPerfil perfilInvestidor;

    public static UsuarioDTO fromModel(Usuario usuario){
        UsuarioDTO dto = new UsuarioDTO();
        dto.setNome(usuario.getNome());
        dto.setEmail(usuario.getEmail());
        dto.setPerfilInvestidor(usuario.getPerfilInvestidor());
        return dto;
    }

    public static Usuario toEntity(UsuarioDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setPerfilInvestidor(dto.getPerfilInvestidor());
        return usuario;
    }
}
