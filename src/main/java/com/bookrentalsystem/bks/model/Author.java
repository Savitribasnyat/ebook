package com.bookrentalsystem.bks.model;

import com.bookrentalsystem.bks.model.auditing.Auditable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Table(name = "author",uniqueConstraints = {
        @UniqueConstraint(name = "uk_author_email",columnNames = "email"),
        @UniqueConstraint(name = "uk_author_number",columnNames = "phone_number")
})
@SQLDelete(sql = "UPDATE author SET deleted=true WHERE id = ?")  //this is used for soft delete it helps to change the deleted status to true
@Where(clause = "deleted = false")
public class Author extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "author_name",nullable = false)
    private String name;

    @Column(name = "email",length = 100,nullable = false)
    private String email;

    @Column(name = "phone_number",length = 10)
    private String number;

    private Boolean deleted=Boolean.FALSE;

}
