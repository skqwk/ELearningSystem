package ru.skqwk.elearningsystem.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.skqwk.elearningsystem.model.Course;
import ru.skqwk.elearningsystem.model.Group;
import ru.skqwk.elearningsystem.model.Teacher;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Builder
@Getter
@Setter
@Entity
@Table(name="courses_teachers_groups")
@NoArgsConstructor
@AllArgsConstructor
public class CourseTeacherGroup {
    @Id
    @SequenceGenerator(
            name = "course_teacher_group_sequence",
            sequenceName = "course_teacher_group_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "course_teacher_group_sequence"
    )
    private Long id;

    @ManyToOne
    private Group group;

    @ManyToOne
    private Course course;

    @ManyToOne
    private Teacher teacher;
}
