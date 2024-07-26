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
@NoArgsConstructor
@Builder
@Table(name = "category")
@SQLDelete(sql = "UPDATE category SET deleted=true WHERE id = ?")  //this is used for soft delete it helps to change the deleted status to true
@Where(clause = "deleted = false")
public class Category extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "category_name",nullable = false)
    private String name;
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    private Boolean deleted = Boolean.FALSE;


}
