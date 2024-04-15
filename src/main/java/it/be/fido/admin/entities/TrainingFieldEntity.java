package it.be.fido.admin.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "training_fields",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "name")
        })
@Getter
@Setter
@NoArgsConstructor
public class TrainingFieldEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        private Long id;

        @NotBlank
        @Size(max = 40)
        @Column(name = "name")
        private String name;

        @Column(name = "detail")
        private String detail;

        @OneToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "activity_id")
        private ActivityEntity activity;
}
