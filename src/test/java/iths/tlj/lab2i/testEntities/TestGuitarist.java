package iths.tlj.lab2i.testEntities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="guitarists")

public class TestGuitarist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String firstName;
    private String lastName;
    private String nationality;

    public TestGuitarist(int id, String firstName, String lastName, String nationality) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.nationality = nationality;
    }

    public TestGuitarist() {

    }

}
