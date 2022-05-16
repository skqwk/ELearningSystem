package ru.skqwk.elearningsystem.dao;

import org.springframework.stereotype.Repository;
import ru.skqwk.elearningsystem.model.EducationalMaterial;
import ru.skqwk.elearningsystem.model.Student;
import ru.skqwk.elearningsystem.model.StudyStatus;

@Repository
public interface StudyStatusDao extends AbstractDao<StudyStatus> {


    StudyStatus findByStudentAndMaterial(Student student, EducationalMaterial material);
}
