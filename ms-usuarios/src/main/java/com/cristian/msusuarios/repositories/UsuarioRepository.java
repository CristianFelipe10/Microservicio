package com.cristian.msusuarios.repositories;

import com.cristian.msusuarios.models.entity.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UsuarioRepository extends CrudRepository<Usuario, Long> {
    //Diferentes formas de validar email
    Optional<Usuario> findByEmail(String email);

    //@Query("select u from usuarios where u.email = ?1")
    //Optional<Usuario> obtenerPorEmail(String email);

    boolean existsByEmail(String email);
}
