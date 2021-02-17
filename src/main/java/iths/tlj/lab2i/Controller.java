package iths.tlj.lab2i;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping("/guitarists")
    public List<Guitarist> sayHello(){


        return "Shred on, dude.";
    }

}
