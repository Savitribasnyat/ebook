package com.bookrentalsystem.bks.model;

import com.bookrentalsystem.bks.enums.RequestedBook;
import com.bookrentalsystem.bks.model.login.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "book_request")
public class RequestBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "book_name", length = 100, nullable = false)
    @NotBlank(message = "please provide book name!!")
    @Length(max = 100,min = 2,message = "Book name should be between 3 to 100")
    private String name;

    @Column(name = "author_name", length = 100, nullable = false)
    @NotBlank(message = "Name should not be blank")
    @Length(max = 100,min = 3,message = "Name should be between 3 to 100 word ")
    private String authorName;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "bookrequest_user",referencedColumnName = "id")
    private User user;

    @Enumerated(EnumType.STRING)
    private RequestedBook status;
}
