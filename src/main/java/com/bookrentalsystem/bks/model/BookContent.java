package com.bookrentalsystem.bks.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "book_content")
@Data
@AllArgsConstructor
public class BookContent {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "book_content_seq_gen")
    @SequenceGenerator(name = "book_content_seq_gen",sequenceName = "book_content_seq",allocationSize = 1)
    private Long id;

    private String fileName;

    private String fileType;

    @Lob
    private byte[] data;

    public BookContent(String fileName, String fileType, byte[] data) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.data = data;
    }

    public BookContent() {

    }
}
