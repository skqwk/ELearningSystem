package ru.skqwk.elearningsystem.dao;

import ru.skqwk.elearningsystem.model.EducationalMaterial;
import ru.skqwk.elearningsystem.model.dto.CourseTeacherGroup;

import java.util.List;

public interface EducationalMaterialDao extends AbstractDao<EducationalMaterial>{
    List<EducationalMaterial> findAllByCourseTeacherGroup(CourseTeacherGroup courseTeacherGroup);
}
