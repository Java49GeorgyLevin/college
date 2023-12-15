package telran.college.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import telran.college.dto.*;
import telran.college.entities.*;
import telran.college.repo.*;
import telran.exceptions.NotFoundException;
@Service
@RequiredArgsConstructor
@Transactional(readOnly=true)
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

	@Override
	@Transactional(readOnly = false)
	public PersonDto addStudent(PersonDto personDto) {
		if(studentRepo.existsById(personDto.id())) {
			throw new IllegalStateException(personDto.id() + " already exists");
		}
		studentRepo.save(new Student(personDto));
		return personDto;
	}

	@Override
	@Transactional(readOnly = false)
	public PersonDto addLecturer(PersonDto personDto) {
		if(lecturerRepo.existsById(personDto.id())) {
			throw new IllegalStateException(personDto.id() + " already exists");
		}
		lecturerRepo.save(new Lecturer(personDto));
		return personDto;
	}

	@Override
	@Transactional(readOnly = false)
	public SubjectDto addSubject(SubjectDto subjectDto) {
		Lecturer lecturer = null;
		Long lecturerId = subjectDto.lecturerId();
		if(lecturerId != null) {
			lecturer = lecturerRepo.findById(lecturerId)
					.orElseThrow(() -> new NotFoundException(lecturerId + " not exists"));
		}
		Subject subject = new Subject(subjectDto);
		subject.setLecturer(lecturer);
		subjectRepo.save(subject);
		return subjectDto;
	}

	@Override
	@Transactional(readOnly = false)
	public MarkDto addMark(MarkDto markDto) {

		long stId = markDto.studentId();
		Student student = studentRepo.findById(stId)
				.orElseThrow(() -> new NotFoundException(stId + " not exists"));
		
		long suId = markDto.subjectId();
		Subject subject = subjectRepo.findById(suId)
				.orElseThrow(() -> new NotFoundException(suId + " not exists"));
		
		Mark mark = new Mark(student, subject, markDto.score());
		markRepo.save(mark);
		return markDto;
	}

	@Override
	@Transactional(readOnly = false)
	public PersonDto updateStudent(PersonDto personDto) {
		Student student = studentRepo.findById(personDto.id())
				.orElseThrow(() -> new NotFoundException(personDto.id() + " not exists"));
		student.setCity(personDto.city());
		student.setPhone(personDto.phone());
		return personDto;
	}

	@Override
	@Transactional(readOnly = false)	
	public PersonDto updateLecturer(PersonDto personDto) {
		long id = personDto.id();
		Lecturer lecturer = lecturerRepo.findById(id)
				.orElseThrow(() -> new NotFoundException(id + " not exists"));
		lecturer.setCity(personDto.city());
		lecturer.setPhone(personDto.phone());
		return lecturer.build();
	}

	@Override
	@Transactional(readOnly = false)
	public PersonDto deleteLecturer(long id) {
		Lecturer lecturer = lecturerRepo.findById(id)
				.orElseThrow(() -> new NotFoundException(id + " not exists"));
		
		List<Subject> subjects = subjectRepo.findByLecturerId(id);
		subjects.forEach(s -> s.setLecturer(null));
		lecturerRepo.deleteById(id);
		return lecturer.build();
	}



	@Override
	@Transactional(readOnly = false)
	public SubjectDto deleteSubject(long id) {
		Subject subject = subjectRepo.findById(id)
				.orElseThrow(() -> new NotFoundException(id + " not exists"));
		List<Mark> marks = markRepo.findBySubjectId(id);
		marks.forEach(m -> markRepo.delete(m));
		subjectRepo.deleteById(id);
		return subject.build();
	}

	@Override
	@Transactional(readOnly = false)
	public List<PersonDto> deleteStudentsHavingScoresLess(int nScores) {
		List<Student> students = studentRepo.getStudentsHavingScoresLess(nScores);		
				
		students.forEach(s -> {
			long id = s.getId();
			List<Mark> marks = markRepo.findByStudentId(id);
			marks.forEach(m -> markRepo.delete(m));			
			studentRepo.deleteById(id);
		});
		return students.stream().map(s -> s.build()).toList();
	}

}
