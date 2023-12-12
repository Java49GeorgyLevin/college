package telran.college.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import telran.college.dto.*;
import telran.college.entities.*;

public interface MarkRepo extends JpaRepository<Mark, Long> {
	
	@Query("Select student.name as name, round(avg(score)) as score from Mark "
			+ "group by student.name "
			+ "order by avg(score) desc")
	List<StudentMark> getStudentsAvgMarks();
	
	List<SubjectNameScore> findByStudentName(String nameStudent);
	
	@Query("SELECT student.name from Mark where subject.type=:type "
			+ "group by student.name order by avg(score) desc limit :nStudents")	
	List<String> findBestStudentsSubjectType(SubjectType type, int nStudents);	
	
	@Query("SELECT st.name as name, st.city as city, count(m.score) as count from Mark m "
			+ "right join m.student st "
			+ "group by st.name, city having count(m.score) < :score")
	List<NameCity> getNameCityScoresLess(int score);

}
