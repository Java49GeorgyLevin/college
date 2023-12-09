package telran.college.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import telran.college.dto.*;

import telran.college.entities.*;

public interface StudentRepo extends JpaRepository<Student, Long> {
	String JOIN_STUDENTS_MARKS = "FROM students_lecturers st join marks m on stid=st.id ";
	String JOIN_ALL = JOIN_STUDENTS_MARKS
			+ "join subjects sb on sb.id=suid ";
	@Query(value = "SELECT st.name as name " + JOIN_ALL + "where type=:type "
			+ "group by st.name order by avg(score) desc limit :nStudents",
			nativeQuery = true)
	List<String> findBestStudentsSubjectType(String type, int nStudents);
	@Query(value="SELECT st.name as name, round(avg(score)) as score " + JOIN_STUDENTS_MARKS
			+ " group by st.name order by avg(score) desc",
			nativeQuery = true)
	List<StudentMark> getStudentsAvgMarks();
	@Query(value="select distinct st.name, city "
			+ JOIN_STUDENTS_MARKS
			+ "where score < :score order by st.name",
			nativeQuery=true)
	List<NameCityPhone> getNameCityUnderachieving(float score);
	
	//TODO
	@Query(value="select name, phone as phone, birth_date as birthDate from students_lecturers "
//			+ "where dType='Student' "
			+ "where DATEPART(month, birth_date) = 10 ",
			nativeQuery=true)
	List<NameCityPhone> getNamesCitiesByMonth(Months month);
	
	@Query(value="select sb.name as subject, score as score "
			+ JOIN_ALL+
			"where st.name =:nameStudent order by sb.name",
			nativeQuery=true)
	List<SubjectScore> getSubjectsScoresbyStudentName(String nameStudent);

}
