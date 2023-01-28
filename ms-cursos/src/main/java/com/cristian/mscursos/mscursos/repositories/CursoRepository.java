package com.cristian.mscursos.mscursos.repositories;

import com.cristian.mscursos.mscursos.models.entity.Curso;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CursoRepository extends CrudRepository<Curso, Long> {

    //Se pone la anotación cuando se va a eliminar, insertar o actualizar para que se ejecute
    @Modifying
    @Query("delete from CursoUsuario cu where cu.usuarioId=?1")
    void eliminarCursoUsuarioPorId(Long id);
}
