package iths.tlj.lab2i;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class Controller {

    //Using dependency injection here; if we were to create a new instance below, we would need all the @Override
    // methods in the code
    private GuitaristRepository guitaristRepository;

    //Autowired annotation ensures that this is automatically created when a Controller class is made
    @Autowired
    public Controller(GuitaristRepository guitaristRepository) {
        this.guitaristRepository = guitaristRepository;
    }

    //TEST HTTP METHOD
    @GetMapping("/test")
    public String test() {
        return "Here I am";
    }

    //CREATE METHODS
    //If you make a post request at this address you will create a new Json object in the database.
    @PostMapping("/guitarist")
    @ResponseStatus(HttpStatus.CREATED)     //Gives a 201 CREATED response instead of simply 200 OK.
    public ResponseEntity<?> createGuitarist(@RequestBody Guitarist guitarist) {
        if (guitaristRepository.existsById(guitarist.getId())){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE);
        }else
        System.out.println("New guitarist object created");
        guitaristRepository.save(guitarist);
        return ResponseEntity.ok("New entry to database:\n" + guitarist.getId() + "\n" + guitarist.getFirstName() +
                        "\n" + guitarist.getLastName() + "\n" + guitarist.getNationality() + "\nhas been created.");
    }

    //READ METHODS
    //map method to url. The get request at url returns list of all users
    @GetMapping("/guitarists")
    public List<Guitarist> listAllGuitarists() {
        return guitaristRepository.findAll();
    }

    //get request at url returns single object from id
    @GetMapping("/id/{id}")
    public Guitarist findGuitaristById(@PathVariable int id) {
        if(!guitaristRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }else
        return guitaristRepository.findById(id)
                .orElse(new Guitarist());
    }

    @GetMapping("/firstname/{firstname}")
    public List<Guitarist> findGuitaristsByFirst(@PathVariable String firstname) {
        if (guitaristRepository.findAllByFirstName(firstname).isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }else
        return guitaristRepository.findAllByFirstName(firstname);
    }

    @GetMapping("/lastname/{lastname}")
    public List<Guitarist> findGuitaristsByLast(@PathVariable String lastname) {
        if (guitaristRepository.findAllByLastName(lastname).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }else
        return guitaristRepository.findAllByLastName(lastname);
    }

    @GetMapping("/nationality/{nationality}")
    public List<Guitarist> findGuitaristsByNationality(@PathVariable String nationality) {
        if (guitaristRepository.findAllByNationality(nationality).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }else
        return guitaristRepository.findAllByNationality(nationality);
    }

    //UPDATE METHODS
    @PutMapping("update")
    public ResponseEntity<?> updateGuitarist(@RequestBody Guitarist guitarist) {
        if (!guitaristRepository.existsById(guitarist.getId())){
            throw new ResponseStatusException(HttpStatus.NOT_MODIFIED);
        }else
            guitaristRepository.save(guitarist);
        System.out.println("Guitarist has been updated.");
        return ResponseEntity.ok("Entry updated to:\n" + guitarist.getId() + "\n" + guitarist.getFirstName() +
                "\n" + guitarist.getLastName() + "\n" + guitarist.getNationality());
    }

    //DELETE METHODS
    //url for deleting an object. If the ID does not exist, a bad request exception is thrown.
    //@PostMapping("/delete")
    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> deleteGuitarist(@RequestBody Guitarist guitarist) {
        if(guitaristRepository.existsById(guitarist.getId())) {
            guitaristRepository.delete(guitarist);
            System.out.println("Guitarist deleted.");
            return ResponseEntity.ok("Deleted entry with ID: " + guitarist.getId());
        } else
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
}
