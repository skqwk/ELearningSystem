package ru.skqwk.elearningsystem.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.skqwk.elearningsystem.model.enumeration.Status;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudyStatus {


    @Id
    @SequenceGenerator(
            name = "study_status_sequence",
            sequenceName = "study_status_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "study_status_sequence"
    )
    private Long id;

    @OneToOne
    private Student student;

    @OneToOne
    private EducationalMaterial material;

    private Status status;
    private String content;
}
