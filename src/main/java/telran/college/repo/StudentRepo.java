package telran.college.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import telran.college.dto.*;

import telran.college.entities.*;

public interface StudentRepo extends JpaRepository<Student, Long> {

	@Query("select st.name as name, st.city as city from Student st "
			+ "where extract(month from st.birthDate) = :month")
	List<NameCity> getNamesCitiesByMonth(int month);	

}
