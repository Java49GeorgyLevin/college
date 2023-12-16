package telran.college;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import telran.college.dto.*;
import telran.college.entities.*;
import telran.college.service.CollegeService;
import telran.exceptions.NotFoundException;
@SpringBootTest
@Sql(scripts= {"db_test.sql"})
class CollegeServiceTest {
@Autowired
CollegeService collegeService;
	@Test
	void bestStudentsTypeTest() {
		List<String> students = collegeService.bestStudentsSubjectType("BACK_END", 2);
		String[] expected = {"David", "Yosef"};
		assertArrayEquals(expected, students.toArray(String[]::new));
	}
	@Test 
	void studentsAverageTest() {
		List<StudentMark> studentsMarks = collegeService.studentsAvgMarks();
		//HW-p1.1
		Map<String, Integer> expSet = Map.of("David", 96, "Rivka", 95, "Vasya", 83, "Sara", 80, "Yosef", 78, "Yakob", 0);
		studentsMarks.forEach(sm -> assertEquals(expSet.get(sm.getName()), sm.getScore()));
		assertEquals(expSet.size(), studentsMarks.size());		
	}
	//HW-p2
	@Test
	void lecturersHiScoreHoursTest() {
		List<LecturerHours> lecturersHours = collegeService.lecturersHiScoreHours(2);
		Map<String, Integer> expMap = Map.of("Abraham", 225, "Mozes", 130);
		lecturersHours.forEach(lh -> assertEquals(expMap.get(lh.getName()), lh.getHours()));
		assertEquals(expMap.size(), lecturersHours.size());		
	}
	//HW-p3
	@Test
	void namesCitiesScoresLessStudentsTest() {
		List<NameCity> nameCity = collegeService.nameCityScoresLess(4);
		Map<String, String> expMap = Map.of("Rivka", "Lod", "Yakob", "Rehovot", "Yosef", "Rehovot");		
		nameCity.forEach(nc -> assertEquals(expMap.get(nc.getName()), nc.getCity()));		
		assertEquals(expMap.size(), nameCity.size());		
	}
	
	//HW-p4
	@Test
	void namesCitiesByMonthTest() {
		List<NameCity> nameCity = collegeService.namesCitiesByMonth(10);
		String[] students = {"Vasya", "Yakob"};
		String[] cities = {"Rehovot", "Rehovot"};
		NameCity[] arNameCity = nameCity.toArray(NameCity[]::new);
		
		IntStream.range(0, students.length).forEach(i -> {
			assertEquals(students[i], arNameCity[i].getName());
			assertEquals(cities[i], arNameCity[i].getCity());
			}		
		);
	}
	
	//HW-p5
	@Test
	void subjectsScoresbyStudentNameTest() {
		List<SubjectNameScore> subjectsScores = collegeService.subjectsScoresbyStudentName("Vasya");
		Map<String, Float> expMap = Map.of( "HTML/CSS", 95.0f, 
		"Java Core", 75.0f,
		"Java Technologies", 60.0f,
		"JavaScript", 85.0f,
		"React", 100.0f);
		subjectsScores.forEach(sSc -> assertEquals(expMap.get(sSc.getSubjectName()), sSc.getScore()));
		assertEquals(expMap.size(), subjectsScores.size());
	}
	//HW-p6
	@Test
	void lecturersNamesPhonesByCityTest() {
		List<NamePhone> lecturerNamePhone = collegeService.lecturersNamesPhonesByCity("Jerusalem");
		Map<String, String> expMap = Map.of("Abraham", "050-1111122", "Mozes", "054-3334567");
		lecturerNamePhone.forEach(nP -> assertEquals(expMap.get(nP.getName()), nP.getPhone()));
		assertEquals(expMap.size(), lecturerNamePhone.size());
	}
	@Test
	@Sql(scripts= {"db_test.sql"})
	void addStudentTest() {		
		PersonDto personDto = new PersonDto(129, "John", LocalDate.of(1990, 12, 15), "New York", "055-1234567");		
		PersonDto personDtoReturn = collegeService.addStudent(personDto);
		assertEquals(personDto, personDtoReturn);
		Student student = new Student(personDto);
		student.equals(collegeService.getStudentById(129));
		assertThrowsExactly(IllegalStateException.class, () -> collegeService.addStudent(personDto));
	}
	@Test
	@Sql(scripts= {"db_test.sql"})
	void addLecturerTest() {
		PersonDto personDto = new PersonDto(1233, "Evripid", LocalDate.of(1950, 5, 10), "Bnei Brak", "056-1234567");
		PersonDto personDtoReturn = collegeService.addLecturer(personDto);
		personDto.equals(personDtoReturn);
		Lecturer lecturer = new Lecturer(personDto);
		assertEquals(lecturer.getName(), collegeService.getLecturerById(1233).getName());
		assertThrowsExactly(IllegalStateException.class, () -> collegeService.addLecturer(personDto));
	}
	@Test
	@Sql(scripts ={"db_test.sql"})
	void addSubjectTest() {
		SubjectDto subjectDto = new SubjectDto(326, "SQL", 50, null, SubjectType.BACK_END);
		SubjectDto subjectDtoReturn = collegeService.addSubject(subjectDto);
		assertEquals(subjectDto, subjectDtoReturn);
				
		SubjectDto subjectDtoLecturerNotExists = new SubjectDto(326, "SQL", 50, 1233L, SubjectType.BACK_END);
		assertThrowsExactly(NotFoundException.class, () -> collegeService.addSubject(subjectDtoLecturerNotExists));
				
		Lecturer lecturer = new Lecturer(
		collegeService.addLecturer(new PersonDto(1233, "Evripid", LocalDate.of(1950, 5, 10), "Bnei Brak", "056-1234567")));
		subjectDto = new SubjectDto(326, "SQL", 50, 1233L, SubjectType.BACK_END);
		subjectDtoReturn = collegeService.addSubject(subjectDto);
		assertEquals("Evripid", lecturer.getName());
		assertEquals(subjectDto, subjectDtoReturn);
	}
	@Test
	@Sql(scripts = {"db_test.sql"})
	void addMarkTest() {
		collegeService.addStudent(new PersonDto(129, "John", LocalDate.of(1990, 12, 15), "New York", "055-1234567"));
		collegeService.addLecturer(new PersonDto(1233, "Evripid", LocalDate.of(1950, 5, 10), "Bnei Brak", "056-1234567"));
		collegeService.addSubject(new SubjectDto(326, "SQL", 50, 1233L, SubjectType.BACK_END));
		
		MarkDto markDto = new MarkDto(129, 326, 65);
		MarkDto markDtoReturn = collegeService.addMark(markDto);
		assertEquals(markDto, markDtoReturn);
		
		MarkDto markDtoStudentNotExists = new MarkDto(130, 326, 65);
		assertThrowsExactly(NotFoundException.class, () -> collegeService.addMark(markDtoStudentNotExists));
		
		MarkDto markDtoSubjectNotExists = new MarkDto(129, 327, 65);
		assertThrowsExactly(NotFoundException.class, () -> collegeService.addMark(markDtoSubjectNotExists));		
	}	
	@Test
	@Sql(scripts = {"db_test.sql"})
	void deleteLecturerTest() {
		long id = 1232;
		Lecturer lecturer = collegeService.getLecturerById(id);
		PersonDto personDtoReturn =  collegeService.deleteLecturer(id);
		personDtoReturn.equals(lecturer.build());
		lecturer.equals(new Lecturer(personDtoReturn));
		assertThrowsExactly(NotFoundException.class, () -> collegeService.deleteLecturer(id));
		assertEquals(null, collegeService.getSubjectById(325).getLecturer());
	}
	@Test
	@Sql(scripts = {"db_test.sql"})
	void deleteStudentsHavingScoresLessTest() {
		collegeService.addStudent(new PersonDto(129, "John", LocalDate.of(1990, 12, 15), "New York", "055-1234567"));
		collegeService.addLecturer(new PersonDto(1233, "Evripid", LocalDate.of(1950, 5, 10), "Bnei Brak", "056-1234567"));
		collegeService.addSubject(new SubjectDto(326, "SQL", 50, 1233L, SubjectType.BACK_END));
		collegeService.addMark(new MarkDto(129, 326, 65));
		Student student125 = collegeService.getStudentById(125);
		Student student126 = collegeService.getStudentById(126);
		Student student128 = collegeService.getStudentById(128);
		Student student129 = collegeService.getStudentById(129);
		List<Student> students = List.of(student125, student126,student128, student129);
		List<PersonDto> personsDto = students.stream().map(s -> s.build()).toList();		
		
		List<PersonDto> personsDtoReturn = collegeService.deleteStudentsHavingScoresLess(3);
		personsDto.equals(personsDtoReturn);
		assertEquals(null, collegeService.getStudentById(125));
	}
}
