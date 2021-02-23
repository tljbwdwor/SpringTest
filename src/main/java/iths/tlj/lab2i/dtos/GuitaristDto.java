package iths.tlj.lab2i.dtos;

import lombok.*;

@Data
@NoArgsConstructor
public class GuitaristDto {
    //data transfer object send info between controller and service
    //we can avoid sending responses to the user in Json format
    private int id;
    private String firstName;
    private String lastName;
    private String nationality;

    public GuitaristDto(int id, String firstName, String lastName, String nationality){
        this.id=id;
        this.firstName=firstName;
        this.lastName=lastName;
        this.nationality=nationality;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }
}
