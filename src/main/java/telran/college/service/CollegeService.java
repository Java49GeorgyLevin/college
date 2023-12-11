package telran.college.service;

import java.util.List;
import telran.college.dto.*;

public interface CollegeService {
	List<String> bestStudentsSubjectType(String type, int nStudents);
	List<StudentMark> studentsAvgMarks();
	List<LecturerHours> lecturersHiScoreHours(int nLecturers);
	List<NameCityPhone> nameCityUnderachieving(float score);
	List<NameCityPhone> namesCitiesByMonth(int month);
	List<SubjectScore> subjectsScoresbyStudentName(String nameStudent);
	List<NameCityPhone> lecturersNamesPhonesByCity(String city);

}
