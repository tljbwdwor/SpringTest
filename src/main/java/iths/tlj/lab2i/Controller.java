package iths.tlj.lab2i;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    //map method to url
    //get request at url returns list of all users
    @GetMapping("/guitarists")
    public List<Guitarist> listAll(){
        return guitaristRepository.findAll();
    }

    //get request at url returns single object from id
    @GetMapping("/guitarist/{id}")
    public Guitarist one(@PathVariable int id){
        return guitaristRepository.findById(id)
                .orElse(new Guitarist());
    }


    //url for adding an object.
    //If you make a post request at this address you will create a new Json object in the database.
    @PostMapping("/guitarists")
    @ResponseStatus(HttpStatus.CREATED)     //Gives a 201 CREATED response instead of simply 200 OK.
    public Guitarist create(@RequestBody Guitarist guitarist){
        return guitaristRepository.save(guitarist);
    }
}



//original get request by id
    /*@GetMapping("/persons/{id}")    //gives you the chance to write a url with id number
    //public Optional<Person> one(@PathVariable Long id){     //Optional can be either a person object or nothing
    //But better this way... much easier code to throw an exception rather than create a ResponseEntity
    public Person one(@PathVariable Long id){
        var result = personRepository.findById(id);
        //return result.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Id " + id + " not found
        // ."));
        // If there is a person here, it comes automatically or else throws exception.
        if(result.isPresent())
            return result.get();
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id " + id + " not found.");
    }*/
