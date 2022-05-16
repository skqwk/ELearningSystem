package ru.skqwk.elearningsystem.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.skqwk.elearningsystem.model.dto.CourseTeacherGroup;
import ru.skqwk.elearningsystem.model.enumeration.MaterialType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="materials")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EducationalMaterial {
    @Id
    @SequenceGenerator(
            name = "educational_material_sequence",
            sequenceName = "educational_material_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "educational_material_sequence"
    )
    private Long id;

    private String title;
    private String content;

    private MaterialType type;


    @ManyToOne
    private CourseTeacherGroup courseTeacherGroup;


}
