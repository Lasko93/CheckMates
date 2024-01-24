package com.example.chessappgroupd.domain.pgn;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "PGNFILE")
public class pgnFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PGN_ID")
    private Long id;

    @Column(name = "PGN_USER")
    private String username;

    @Column(name = "PGN_NAME")
    private String fileName;

    @Lob
    @Column(name = "CONTENT")
    private byte[] content;
}
