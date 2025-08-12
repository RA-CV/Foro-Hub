package com.alura.forohub.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "Topico", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"titulo", "mensaje"})
})
@Getter
@Setter
@NoArgsConstructor
public class Topico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String titulo;

    @NotBlank
    private String mensaje;

    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @NotBlank
    private String status = "Abierto";

    @Column(name = "autor_id", nullable = false)
    private Long autorId;

    @Column(name = "curso_id", nullable = false)
    private Long cursoId;

    public Topico(String titulo, String mensaje, Long autorId, Long cursoId) {
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.autorId = autorId;
        this.cursoId = cursoId;
        this.fechaCreacion = LocalDateTime.now();
        this.status = "Abierto";
    }
}
