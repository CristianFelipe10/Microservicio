package com.cristian.msusuarios.controllers;

import com.cristian.msusuarios.models.entity.Usuario;
import com.cristian.msusuarios.services.UsuarioService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @GetMapping
    public List<Usuario> listar(){
        return service.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalle(@PathVariable Long id){
        Optional<Usuario> usuarioOptional = service.porId(id);
        if(usuarioOptional.isPresent()){
            return ResponseEntity.ok(usuarioOptional.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> guardar(@Valid @RequestBody Usuario usuario, BindingResult result){
        if(result.hasErrors()){
            return validar(result);
        }

        if(!usuario.getEmail().isEmpty() && service.existsByEmail(usuario.getEmail())){
            return ResponseEntity.badRequest()
                    .body(Collections
                            .singletonMap("Error", "El correo electronico ya se encuentra registrado"));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(usuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@Valid @RequestBody Usuario usuario, BindingResult result, @PathVariable Long id){
        if(result.hasErrors()){
            return validar(result);
        }

        Optional<Usuario> usuarioOpt = service.porId(id);
        if(usuarioOpt.isPresent()){
            Usuario usuarioDb = usuarioOpt.get();
            if(!usuario.getEmail().isEmpty()
                    && !usuario.getEmail().equalsIgnoreCase(usuarioDb.getEmail())
                    && service.getByEmail(usuario.getEmail()).isPresent()){
                return ResponseEntity.badRequest()
                        .body(Collections
                                .singletonMap("Error", "El correo electronico ya se encuentra registrado"));
            }
            usuarioDb.setNombre(usuario.getNombre());
            usuarioDb.setEmail(usuario.getEmail());
            usuarioDb.setPassword(usuario.getPassword());
            return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(usuarioDb));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id){
        Optional<Usuario> usuarioOpt = service.porId(id);
        if(usuarioOpt.isPresent()){
            service.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/usuarios-por-curso")
    public ResponseEntity<?> obtenerAlumnosPorCurso(@RequestParam List<Long> ids){
        return ResponseEntity.ok(service.listarPorIds(ids));
    }

    private static ResponseEntity<Map<String, String>> validar(BindingResult result) {
        Map<String, String> errores = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errores.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errores);
    }
}
