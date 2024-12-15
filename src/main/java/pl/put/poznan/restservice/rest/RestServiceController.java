package pl.put.poznan.restservice.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.put.poznan.restservice.logic.*;

/**
 * RestServiceController - Class Description
 * *
 * Provides endpoints for processing scenarios, such as generating numbered step list, counting keywords and counting steps.
 * *
 * * @version 1.0
 */
@RestController
@RequestMapping("/RestService")
public class RestServiceController {

    private static final Logger logger = LoggerFactory.getLogger(RestServiceController.class);
    private final ScenarioProcessor scenarioProcessor;


    /**
     * Constructor to initialize controller.
     * @param scenarioProcessor the processor to handle scenario logic
     */
    @Autowired
    public RestServiceController(ScenarioProcessor scenarioProcessor) {
        this.scenarioProcessor = scenarioProcessor;
    }

    /**
     * Default GET endpoint to show a welcome message.
     * This endpoint ensures that the basic page is running.
     * @return a welcome string
     */
    @GetMapping()
    public String showSite() {
        logger.info(("Accessed default GET endpoint"));
        return "Hello! This is just sample site so that it doesn't show error :D";
    }

    /**
     * POST endpoint for generating a numbered list from scenario
     *
     * @param str JSON input of scenario
     * @return numbered list in JSON format of scenario
     * @throws Exception if an error occurs during scenario processing
     */
    @RequestMapping(path = "/numberedStepList", method = RequestMethod.POST, produces = "application/json")
    public String getNumberedList(@RequestBody String str) throws Exception {
        logger.info("Received request to generate numbered list");
        try {
            logger.info("Parsing input");
            NumberListVisitor numberList = new NumberListVisitor();
            Scenario scenario = scenarioProcessor.Proccesing(str);

            logger.debug("Processing scenario with NumberListVisitor class");
            scenario.accept(numberList);

            // For typical txt look
            //String res numberList.getNumberedSteps();

            // For more JSON alike look
            String res = numberList.getJsonSteps();

            logger.info("Successfully generated numbered list");
            logger.debug("Generated JSON:\n" + res);

            return res;

        } catch (Exception e) {
            logger.error("Error while processing: " + e.getMessage() + e);
            return "Error: " + e.getMessage();
        }
    }

    /**
     * POST endpoint for counting keywords in a scenario.
     *
     * @param str JSON input of the scenario.
     * @return keyword count in JSON format.
     */
    @RequestMapping(path ="/keywordCount", method = RequestMethod.POST, produces = "application/json")
    public String getKeywordCount(@RequestBody String str) throws Exception {
        logger.info("Received request to get Keyword Count (KC)");
        logger.debug("KC: Request body: {}", str);
        try {
            logger.info("KC: Starting scenario processing");
            KeywordCountVisitor keywordCountVisitor = new KeywordCountVisitor();
            Scenario scenario = scenarioProcessor.Proccesing(str);
            logger.debug("KC: Scenario processed: {}", scenario);

            logger.info("KC: Starting visitor acceptance on a scenario");
            scenario.accept(keywordCountVisitor);
            logger.debug("KC: Visitor accepted: {}", keywordCountVisitor);

            logger.info("KC: Starting parsing");
            String result = scenarioProcessor.Parsing(keywordCountVisitor);
            logger.debug("KC: Parsing result: {}", result);

            return result;
        } catch (Exception e) {
            logger.error("KC: An error occurred while processing", e);
            return e.getMessage();
        }
    }

    /**
     * POST endpoint for counting steps in a scenario.
     *
     * @param str JSON input of the scenario.
     * @return steps count in JSON format.
     */
    @RequestMapping(path ="/stepCount", method = RequestMethod.POST, produces = "application/json")
    public String getStepCount(@RequestBody String str) throws Exception {
        logger.info("Received request to get Step Count (SC)");
        logger.debug("SC: Request body: {}", str);
        try {
            logger.info("SC: Starting scenario processing");
            StepCountVisitor stepCountVisitor = new StepCountVisitor();
            Scenario scenario = scenarioProcessor.Proccesing(str);
            logger.debug("SC: Scenario processed: {}", scenario);

            logger.info("SC: Starting visitor acceptance on a scenario");
            scenario.accept(stepCountVisitor);
            logger.debug("SC: Visitor accepted: {}", stepCountVisitor);

            logger.info("SC: Starting parsing");
            String result = scenarioProcessor.Parsing(stepCountVisitor);
            logger.debug("SC: Parsing result: {}", result);

            return result;

        }
        catch (Exception e) {
            logger.error("KC: An error occurred while processing", e);
            return e.getMessage();
        }
    }

}


