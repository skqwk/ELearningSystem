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
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Entity
@Table(name="groups")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Group {
    @Id
    @SequenceGenerator(
            name = "group_sequence",
            sequenceName = "group_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "group_sequence"
    )
    private Long id;

    private String literal;
    private int number;

    @OneToMany(
            cascade = {
                    CascadeType.REFRESH},
            fetch = FetchType.EAGER,
            mappedBy = "group"
    )
    @Fetch(value = FetchMode.SELECT)
    private List<Student> students = new ArrayList<>();

//    @ManyToMany(
//            cascade = {
//                    CascadeType.MERGE,
//                    CascadeType.REFRESH},
//            fetch = FetchType.EAGER,
//            mappedBy = "groups"
//    )
//    @Fetch(value = FetchMode.SELECT)
//    private List<Course> courses = new ArrayList<>();

    @OneToMany(
            cascade = {CascadeType.REMOVE,
                    CascadeType.REFRESH
            },
            mappedBy = "group",
            fetch = FetchType.EAGER
    )
    @Fetch(value = FetchMode.SELECT)
    private List<CourseTeacherGroup> courseTeacherGroups = new ArrayList<>();
}
