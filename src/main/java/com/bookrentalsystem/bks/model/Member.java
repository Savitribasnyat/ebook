package com.bookrentalsystem.bks.model;

import com.bookrentalsystem.bks.model.auditing.Auditable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "members",uniqueConstraints = {
        @UniqueConstraint(name = "Uk_members_email",columnNames = "email_address"),
        @UniqueConstraint(name = "Uk_members_phone",columnNames = "phone_number")
})
@SQLDelete(sql = "UPDATE members SET deleted=true WHERE id = ?")  //this is used for soft delete it helps to change the deleted status to true
@Where(clause = "deleted = false")
public class Member extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "member_name",nullable = false,length = 100)
    private String name;
    @Column(name = "email_address",length = 100,nullable = false)
    private String email;
    @Column(name = "member_address")
    private String address;
    @Column(name = "phone_number",length = 10)
    private String phone;
    private Boolean deleted = Boolean.FALSE;
}
