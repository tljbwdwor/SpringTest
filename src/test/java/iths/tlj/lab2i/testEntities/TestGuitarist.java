package iths.tlj.lab2i.testEntities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
//@Table(name="guitarists")

public class TestGuitarist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String firstname;
    private String lastname;
    private String nationality;

    public TestGuitarist(int id, String firstname, String lastname, String nationality) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.nationality = nationality;
    }
}
