package it.be.izi.dog.entities;

import it.be.izi.dog.enumerations.ERole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 25, name = "name")
    private ERole name;

    public RoleEntity(ERole erole) {
        this.name = erole;
    }
}