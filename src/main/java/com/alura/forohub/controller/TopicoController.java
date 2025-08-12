package com.alura.forohub.controller;

import com.alura.forohub.dto.DatosActualizarTopico;
import com.alura.forohub.dto.DatosDetalleTopico;
import com.alura.forohub.dto.DatosListadoTopico;
import com.alura.forohub.dto.DatosRegistroTopico;
import com.alura.forohub.model.Topico;
import com.alura.forohub.repository.TopicoRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    private final TopicoRepository topicoRepository;

    public TopicoController(TopicoRepository topicoRepository) {
        this.topicoRepository = topicoRepository;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> registrarTopico(@RequestBody @Valid DatosRegistroTopico datos) {
        // Regla: evitar duplicados
        if (topicoRepository.existsByTituloAndMensaje(datos.titulo(), datos.mensaje())) {
            return ResponseEntity.badRequest().body("Error: Ya existe un tópico con el mismo título y mensaje.");
        }

        Topico nuevoTopico = new Topico(
                datos.titulo(),
                datos.mensaje(),
                datos.autorId(),
                datos.cursoId()
        );
        topicoRepository.save(nuevoTopico);
        return ResponseEntity.ok(nuevoTopico);

    }

    @GetMapping
    public ResponseEntity<Page<Topico>> listarTopicos(
            @RequestParam(required = false) Long cursoId,
            @RequestParam(required = false) Integer anio,
            @PageableDefault(size = 10, sort = "fechaCreacion", direction = Sort.Direction.ASC) Pageable pageable) {

        Page<Topico> pagina;

        // Filtro por curso y año (opcional)
        if (cursoId != null && anio != null) {
            pagina = topicoRepository.buscarPorCursoYAnio(cursoId, anio, pageable);
        } else {
            pagina = topicoRepository.findAll(pageable);
        }

        return ResponseEntity.ok(pagina);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerDetalleTopico(@PathVariable Long id) {
        return topicoRepository.findById(id)
                .map(topico -> ResponseEntity.ok(
                        new DatosDetalleTopico(
                                topico.getTitulo(),
                                topico.getMensaje(),
                                topico.getFechaCreacion(),
                                topico.getStatus(),
                                topico.getAutorId(),
                                topico.getCursoId()
                        )
                ))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> actualizarTopico(
            @PathVariable Long id,
            @RequestBody @Valid DatosActualizarTopico datos
    ) {
        Optional<Topico> optionalTopico = topicoRepository.findById(id);

        if (optionalTopico.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Topico topico = optionalTopico.get();

        // Regla: evitar duplicados (mismo título y mensaje en otro tópico)
        if (topicoRepository.existsByTituloAndMensaje(datos.titulo(), datos.mensaje())
                && (!topico.getTitulo().equals(datos.titulo()) || !topico.getMensaje().equals(datos.mensaje()))) {
            return ResponseEntity.badRequest().body("Error: Ya existe un tópico con el mismo título y mensaje.");
        }

        // Actualización de campos
        topico.setTitulo(datos.titulo());
        topico.setMensaje(datos.mensaje());
        topico.setAutorId(datos.autorId());
        topico.setCursoId(datos.cursoId());

        return ResponseEntity.ok(new DatosDetalleTopico(
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getFechaCreacion(),
                topico.getStatus(),
                topico.getAutorId(),
                topico.getCursoId()
        ));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> eliminarTopico(@PathVariable Long id) {
        Optional<Topico> optionalTopico = topicoRepository.findById(id);

        if (optionalTopico.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        topicoRepository.deleteById(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

}
