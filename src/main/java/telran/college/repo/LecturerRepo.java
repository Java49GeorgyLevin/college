package telran.college.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import telran.college.dto.LecturerHours;
import telran.college.dto.NameCityPhone;
import telran.college.entities.*;

public interface LecturerRepo extends JpaRepository<Lecturer, Long> {
	String SUBJECTS_LECTURERS = "from subjects sb join students_lecturers lc on lecturer_id=lc.id ";

	@Query(value ="SELECT lc.name as name, sum(hours) as hours " 
			+ SUBJECTS_LECTURERS
			+ "group by lc.name order by sum(hours) desc limit :nLecturers",
			nativeQuery = true)
	List<LecturerHours> getLecturersHiScoreHours(int nLecturers);
	
	@Query(value="select name, phone from students_lecturers "
			+ "where dType='Lecturer' AND city=:city order by name",
			nativeQuery=true)
	List<NameCityPhone> getLecturersNamesPhonesByCity(String city);

}
