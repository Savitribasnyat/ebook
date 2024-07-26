package com.bookrentalsystem.bks.model;

import com.bookrentalsystem.bks.model.login.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "rating")
public class Rating implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "rating_seq_gen")
    @SequenceGenerator(name = "rating_seq_gen",sequenceName = "rating_seq",allocationSize = 1)
    private Long id;

    @Column(name = "rating_number")
    private float ratingNumber;

//    @Column(name = "rating_post_date")
//    private LocalDateTime ratingPostDate;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "rating_user",referencedColumnName = "id")
    private User user;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name="rating_book",referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "rating_book_fk"))
    private Book book;
}
