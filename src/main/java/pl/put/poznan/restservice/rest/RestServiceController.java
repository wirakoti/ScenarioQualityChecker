package pl.put.poznan.restservice.rest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.put.poznan.restservice.logic.*;


@RestController
@RequestMapping("/RestService")
public class RestServiceController {

    private static final Logger logger = LoggerFactory.getLogger(RestServiceController.class);
    private final ScenarioProccesor scenarioProccesor;

    @Autowired
    public RestServiceController(ScenarioProccesor scenarioProccesor) {
        this.scenarioProccesor = scenarioProccesor;
    }

    @RequestMapping(path ="/numberedStepList", method = RequestMethod.POST, produces = "application/json")
    public String getNumberedList(@RequestBody String str) throws Exception {
        try {
            NumberListVisitor numberList = new NumberListVisitor();
            Scenario scenario = scenarioProccesor.Proccesing(str);
            scenario.accept(numberList);
            return numberList.getNumberedSteps();
        }
        catch (Exception e) {
            logger.error(e.getMessage());
            return e.getMessage();
        }
    }



}


