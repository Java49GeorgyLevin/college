package telran.college.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;
import telran.college.dto.*;
import telran.college.entities.*;
import telran.college.repo.*;
import telran.exceptions.NotFoundException;
@Service
@RequiredArgsConstructor

public class CollegeServiceImpl implements CollegeService {
	final StudentRepo studentRepo;
	final LecturerRepo lecturerRepo;
	final SubjectRepo subjectRepo;
	final MarkRepo markRepo;
	final EntityManager em;

	@Override
	@Transactional(readOnly=true)
	public List<String> bestStudentsSubjectType(String type, int nStudents) {
		return markRepo.findBestStudentsSubjectType(SubjectType.valueOf(type), nStudents);
	}

	@Override
	@Transactional(readOnly=true)
	public List<StudentMark> studentsAvgMarks() {		
		return markRepo.getStudentsAvgMarks();
	}

	@Override
	@Transactional(readOnly=true)
	public List<LecturerHours> lecturersHiScoreHours(int nLecturers) {
		return subjectRepo.getLecturersHiScoreHours(nLecturers);
	}

	@Override
	@Transactional(readOnly=true)
	public List<NameCity> nameCityScoresLess(int score) {
		return markRepo.getNameCityScoresLess(score);
	}

	@Override
	@Transactional(readOnly=true)
	public List<NameCity> namesCitiesByMonth(int month) {
		return studentRepo.getNamesCitiesByMonth(month);
	}

	@Override
	@Transactional(readOnly=true)
	public List<SubjectNameScore> subjectsScoresbyStudentName(String nameStudent) {
		return markRepo.findByStudentName(nameStudent);
	}

	@Override
	@Transactional(readOnly=true)
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
		if(subjectRepo.existsById(subjectDto.id())) {
			throw new IllegalStateException("subject " + subjectDto.id() + " already exists");
		}
		
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
		
		lecturerRepo.delete(lecturer);
		return lecturer.build();
	}



	@Override
	@Transactional(readOnly = false)
	public SubjectDto deleteSubject(long id) {
		Subject subject = subjectRepo.findById(id)
				.orElseThrow(() -> new NotFoundException(id + " not exists"));
		subjectRepo.delete(subject);
		return subject.build();
	}

	@Override
	@Transactional(readOnly = false)
	public List<PersonDto> deleteStudentsHavingScoresLess(int nScores) {
		List<Student> students = studentRepo.getStudentsHavingScoresLess(nScores);		
		students.forEach(this::deleteStudent);
		return students.stream().map(Student::build).toList();
	}
	
	void deleteStudent(Student student) {
		studentRepo.delete(student);
	}

	@Override
	public List<String> anyQuery(QueryDto queryDto) {
		String queryStr = queryDto.query();
		List<String> res = null;
		Query query;
		try {
			query = queryDto.queryType() == QueryType.SQL ?
					em.createNativeQuery(queryStr) : em.createQuery(queryStr);
			res = getResult(query);
		} catch (Throwable e) {
			res = List.of(e.getMessage());
		}
		return res;
	}
	@SuppressWarnings("unchecked")
	private List<String> getResult(Query query) {
		List<String> res = Collections.emptyList();
		List<?> resultList = Collections.emptyList();
		try {
			resultList = query.getResultList();
		} catch (Exception e) {
			res = List.of(e.getMessage());
		}
		
		if (!resultList.isEmpty()) {
			res = resultList.get(0).getClass().isArray() ?
					listObjectArraysProcessing((List<Object[]>)resultList) : 
						listObjectsProcessing(resultList);
		}
		return res;
	}
	private List<String> listObjectsProcessing(List<?> resultList) {
		
		try {
			return resultList.stream().map(Object::toString).toList();
		} catch (Exception e) {
			return List.of(e.getMessage());
		}
	}
	private List<String> listObjectArraysProcessing(List<Object[]> resultList) {
		
		return resultList.stream().map(Arrays::deepToString).toList();
	}

}
