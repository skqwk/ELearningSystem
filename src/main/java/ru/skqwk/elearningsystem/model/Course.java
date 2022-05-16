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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="courses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course extends AbstractEntity {
    @Id
    @SequenceGenerator(
            name = "course_sequence",
            sequenceName = "course_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "course_sequence"
    )
    private Long id;

    private String name;

    @ManyToOne
    @NotNull
    private Department department;


//    @ManyToMany(
//            cascade = {
//                    CascadeType.MERGE,
//                    CascadeType.REFRESH},
//            fetch = FetchType.EAGER
//    )
//    @JoinTable(name="groups_courses",
//            joinColumns=@JoinColumn(name="course_id"),
//            inverseJoinColumns=@JoinColumn(name="group_id")
//    )
//    @Fetch(value = FetchMode.SELECT)
//    private List<Group> groups = new ArrayList<>();

    @OneToMany(
            cascade = {CascadeType.REMOVE,
                    CascadeType.REFRESH
            },
            mappedBy = "course",
            fetch = FetchType.EAGER
    )
    @Fetch(value = FetchMode.SELECT)
    private List<CourseTeacherGroup> courseTeacherGroups = new ArrayList<>();

    @Override
    public int hashCode() {
        return Objects.hash(id, name, department, courseTeacherGroups);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Course course = (Course) o;
        return course.getName().equals(this.getName());
    }
}
