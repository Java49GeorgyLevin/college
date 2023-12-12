package telran.college.service;

import java.util.List;
import telran.college.dto.*;

public interface CollegeService {
	List<String> bestStudentsSubjectType(String type, int nStudents);
	List<StudentMark> studentsAvgMarks();
	List<LecturerHours> lecturersHiScoreHours(int nLecturers);
	List<NameCity> nameCityScoresLess(int score);
	List<NameCity> namesCitiesByMonth(int month);
	List<SubjectNameScore> subjectsScoresbyStudentName(String nameStudent);
	List<NamePhone> lecturersNamesPhonesByCity(String city);

}
