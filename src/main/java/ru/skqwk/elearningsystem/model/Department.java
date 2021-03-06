package ru.skqwk.elearningsystem.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="departments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Department {
    @Id
    @SequenceGenerator(
            name = "department_sequence",
            sequenceName = "department_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "department_sequence"
    )
    private Long id;


    private String name;

    @OneToMany(
            cascade = {CascadeType.REMOVE, CascadeType.REFRESH},
            mappedBy = "department",
            fetch = FetchType.EAGER
    )
    @Fetch(value = FetchMode.SELECT)
    private List<Teacher> teachers = new ArrayList<>();

    @OneToMany(
            cascade = {CascadeType.REMOVE, CascadeType.REFRESH},
            mappedBy = "department",
            fetch = FetchType.EAGER
    )
    @Fetch(value = FetchMode.SELECT)
    private List<Course> courses = new ArrayList<>();

}
