package telran.college.entities;

import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;
import telran.college.dto.PersonDto;

@Entity
@NoArgsConstructor
public class Lecturer extends Person {
	public Lecturer(PersonDto personDto) {
		super(personDto);		
	}

	@Override
	public String toString() {
		return "Lecturer [id=" + id + ", name=" + name + ", birthDate=" + birthDate + ", city=" + city + ", phone="
				+ phone + "]";
	}

}
