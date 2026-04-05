package dev.shortn.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tb_url")
public class Url implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url_origem", nullable = false)
    private String originalUrl;

    @Column(name = "codigo_url")
    private String shortCode;

    private LocalDateTime createdAt;
    private Long clicks;

}
