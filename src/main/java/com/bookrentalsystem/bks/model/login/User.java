package com.bookrentalsystem.bks.model.login;

import com.bookrentalsystem.bks.enums.RoleName;
import com.bookrentalsystem.bks.model.Book;
import com.bookrentalsystem.bks.model.Rating;
import com.bookrentalsystem.bks.model.auditing.Auditable;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "users")
public class User extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private RoleName roles ;

//    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "user")
//    @JsonManagedReference(value = "users")
//    private List<Book> books;
//
//
//    @OneToMany(cascade =  CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "user")
//    @JsonManagedReference(value = "users")
//    private List<Rating> ratings;
}
