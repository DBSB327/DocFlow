package com.neoskat.docflow.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "file_name")
    private String fileName;
    @Column(name = "file_path")
    private String filePath;
    @Column(name = "file_size")
    private Long fileSize;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;
}
