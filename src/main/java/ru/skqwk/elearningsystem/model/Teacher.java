package ru.skqwk.elearningsystem.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import ru.skqwk.elearningsystem.model.dto.CourseTeacherGroup;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="teachers")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Teacher {
    @Id
    @SequenceGenerator(
            name = "teacher_sequence",
            sequenceName = "teacher_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "teacher_sequence"
    )
    private Long id;

    @NotEmpty
    private String name = "";
    @NotEmpty
    private String surname = "";
    @NotEmpty
    private String patronymic = "";

    private int workExperience;

    @ManyToOne
//    @JoinColumn(
//            name = "department_id",
//            referencedColumnName = "id"
//    )
    @NotNull
    private Department department;

//    @OneToMany(
//            cascade = {CascadeType.REMOVE, CascadeType.REFRESH},
//            fetch = FetchType.EAGER
//    )
//    @Fetch(value = FetchMode.SELECT)
//    private List<Group> groups;
//
//    @ManyToMany(
//            cascade = {CascadeType.REMOVE, CascadeType.REFRESH},
//            fetch = FetchType.EAGER
//    )
//    @Fetch(value = FetchMode.SELECT)
//    private List<Course> courses;
    @OneToMany(
            cascade = {CascadeType.REMOVE,
                    CascadeType.REFRESH
            },
            mappedBy = "teacher",
            fetch = FetchType.EAGER
    )
    @Fetch(value = FetchMode.SELECT)
    private List<CourseTeacherGroup> courseTeacherGroups = new ArrayList<>();
}
