package telran.college.service;

import java.util.List;
import telran.college.dto.*;
import telran.college.entities.Lecturer;
import telran.college.entities.Student;
import telran.college.entities.Subject;

public interface CollegeService {
	List<String> bestStudentsSubjectType(String type, int nStudents);
	List<StudentMark> studentsAvgMarks();
	List<LecturerHours> lecturersHiScoreHours(int nLecturers);
	List<NameCity> nameCityScoresLess(int score);
	List<NameCity> namesCitiesByMonth(int month);
	List<SubjectNameScore> subjectsScoresbyStudentName(String nameStudent);
	List<NamePhone> lecturersNamesPhonesByCity(String city);
	PersonDto addStudent(PersonDto personDto);
	PersonDto addLecturer(PersonDto personDto);
	SubjectDto addSubject(SubjectDto subjectDto);
	MarkDto addMark(MarkDto markDto);
	PersonDto updateStudent(PersonDto personDto);
	PersonDto updateLecturer(PersonDto personDto);
	PersonDto deleteLecturer(long id);
	SubjectDto deleteSubject(long id);
	List<PersonDto> deleteStudentsHavingScoresLess(int nScores);
	Student getStudentById(long id);
	Lecturer getLecturerById(long id);
	Subject getSubjectById(long id);
	
}
