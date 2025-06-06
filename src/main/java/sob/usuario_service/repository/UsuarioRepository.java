package sob.usuario_service.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import sob.usuario_service.model.Usuario;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends MongoRepository<Usuario, ObjectId> {
    Optional<Usuario> findByEmail(String email);
    void deleteByEmail(String email);
}
