package telran.college.service;

import java.util.List;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import telran.college.dto.*;
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
		return markRepo.findBestStudentsSubjectType(SubjectType.valueOf(type), nStudents);
	}

	@Override
	public List<StudentMark> studentsAvgMarks() {		
		return markRepo.getStudentsAvgMarks();
	}

	@Override
	public List<LecturerHours> lecturersHiScoreHours(int nLecturers) {
		return subjectRepo.getLecturersHiScoreHours(nLecturers);
	}

	@Override
	public List<NameCity> nameCityScoresLess(int score) {
		return markRepo.getNameCityScoresLess(score);
	}

	@Override
	public List<NameCity> namesCitiesByMonth(int month) {
		return studentRepo.getNamesCitiesByMonth(month);
	}

	@Override
	public List<SubjectNameScore> subjectsScoresbyStudentName(String nameStudent) {
		return markRepo.findByStudentName(nameStudent);
	}

	@Override
	public List<NamePhone> lecturersNamesPhonesByCity(String city) {
		return lecturerRepo.findByCity(city);
	}

}
