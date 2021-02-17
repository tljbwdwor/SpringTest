package iths.tlj.lab2i;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

//A repository is in reality Spring automation acting as a DAO and could potentially handle multiple dao's and tables
@Repository
public interface GuitaristRepository extends JpaRepository<Guitarist, Integer> {

    //custom SQL find methods that Spring plugin will auto-generate
    List<Guitarist> findAllByFirstName(String firstname);
    List<Guitarist> findAllByLastName(String lastName);
    List<Guitarist> findAllByNationality(String nationality);
}
