package pl.put.poznan.restservice.rest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.put.poznan.restservice.logic.RestService;

import java.util.Arrays;


@RestController
@RequestMapping("/RestService")
public class RestServiceController {

    private static final Logger logger = LoggerFactory.getLogger(RestServiceController.class);

    private final RestService restService;

    @Autowired
    public RestServiceController(RestService restService) {
        this.restService = restService;
    }


    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public String post(@RequestBody RestService.Scenariusz scen) {

        // log the parameters
        logger.debug(scen.tytul());
        RestService.OdwiedzanieWizytatoraScenariusza  wizytatorScenariusza = new RestService.OdwiedzanieWizytatoraScenariusza();

        return restService.PrzetwarzanieScenariusza(scen, wizytatorScenariusza);

    }



}


