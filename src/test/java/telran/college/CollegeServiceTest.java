package telran.college;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import telran.college.dto.*;

import telran.college.service.CollegeService;
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
		Map<String, Integer> expSet = Map.of("David", 96, "Rivka", 95, "Vasya", 83, "Sara", 80, "Yosef", 78);
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

}
