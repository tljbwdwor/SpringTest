package iths.tlj.lab2i.entities;

import lombok.*;

import javax.persistence.*;

@Data
@Entity
@Table(name="guitarists")
@NoArgsConstructor

public class Guitarist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String firstName;
    private String lastName;
    private String nationality;

    public Guitarist(int id, String firstName, String lastName, String nationality) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.nationality = nationality;
    }
}
