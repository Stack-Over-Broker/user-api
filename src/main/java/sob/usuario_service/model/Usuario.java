package sob.usuario_service.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Document(collection = "usuarios")
public class Usuario {
    @Id
    private String id;

    @Size(min = 3, message = "Nome deve ter no mínimo 3 caracteres")
    private String nome;

    @Email
    private String email;
    private TipoPerfil perfilInvestidor;
    private LocalDate criadoEm = LocalDate.now();
}
