package telran.college.entities;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import telran.college.dro.SubjectType;
@Entity
@Table(name="fj-subjects")
@Getter
@NoArgsConstructor
public class Subject {
	@Id
	long id;
	@Column(nullable = false)
	String name;
	int hours;
	@ManyToOne
	@JoinColumn(name = "lecturer_id")
	Lecturer lecturer;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	SubjectType type;

}