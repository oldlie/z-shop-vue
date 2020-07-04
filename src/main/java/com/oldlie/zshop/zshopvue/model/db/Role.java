package com.oldlie.zshop.zshopvue.model.db;

import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode
@NoArgsConstructor
@Table(name = "t_role", uniqueConstraints = {
        @UniqueConstraint(columnNames = "name")
})
@ToString
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String title;
}
