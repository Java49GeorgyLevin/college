package telran.college.service;

import java.util.List;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import telran.college.dto.LecturerHours;
import telran.college.dto.NameCityPhone;
import telran.college.dto.StudentMark;
import telran.college.dto.SubjectScore;
import telran.college.repo.*;
@Service
@RequiredArgsConstructor
public class CollegeServiceImpl implements CollegeService {
	final StudentRepo studentRepo;
	final LecturerRepo lecturerRepo;
	final SubjectRepo subjectRepo;
	final MarkRepo markRepo;

	@Override
	public List<String> bestStudentsSubjectType(String type, int nStudents) {
		return studentRepo.findBestStudentsSubjectType(type, nStudents);
	}

	@Override
	public List<StudentMark> studentsAvgMarks() {		
		return studentRepo.getStudentsAvgMarks();
	}

	@Override
	public List<LecturerHours> lecturersHiScoreHours(int nLecturers) {
		return lecturerRepo.getLecturersHiScoreHours(nLecturers);
	}

	@Override
	public List<NameCityPhone> nameCityUnderachieving(float score) {
		return studentRepo.getNameCityUnderachieving(score);
	}

	@Override
	public List<NameCityPhone> namesCitiesByMonth(int month) {
		return studentRepo.getNamesCitiesByMonth(month);
	}

	@Override
	public List<SubjectScore> subjectsScoresbyStudentName(String nameStudent) {
		return studentRepo.getSubjectsScoresbyStudentName(nameStudent);
	}

	@Override
	public List<NameCityPhone> lecturersNamesPhonesByCity(String city) {
		return lecturerRepo.getLecturersNamesPhonesByCity(city);
	}

}
