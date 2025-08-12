package com.alura.forohub.repository;

import com.alura.forohub.model.Topico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TopicoRepository extends JpaRepository<Topico, Long> {
    boolean existsByTituloAndMensaje(String titulo, String mensaje);

    // Filtrar por curso y año
    @Query("SELECT t FROM Topico t WHERE t.curso.id = :cursoId AND YEAR(t.fechaCreacion) = :anio")
    Page<Topico> buscarPorCursoYAnio(@Param("cursoId") Long cursoId,
                                     @Param("anio") Integer anio,
                                     Pageable pageable);
}
