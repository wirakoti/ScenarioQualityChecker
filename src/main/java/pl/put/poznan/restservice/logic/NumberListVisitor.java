package pl.put.poznan.restservice.logic;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * NumberListVisitor - Class Description
 * Implements pattern "The Visitor" to process scenarios and generate a numbered list of steps with nesting scenarios.
 *
 * <p>The numbered list of steps is generated in text format with indentation representing the nested levels. Each level adds four indentation spaces.</p>
 * <p>It supports different types of steps, including:</p>
 * <ul>
 *     <li>Basic Steps</li>
 *     <li>IF Steps</li>
 *     <li>For Each Steps</li>
 * </ul>
 *
 * @author CzechowskiMateusz
 * @version 0.1
 */

@Service
public class NumberListVisitor implements ScenarioVisitor {

    // Class Params
    private final List<String> numberedSteps = new ArrayList<>();
    private int stepCounter = 1;    // Counts steps
    private int deepLevel = 0;      // Counts nesting
    private String currentPrefix = "";

    /**
     * Gets Scenario
     *
     * @return numbered script in txt format
     */
    public String getNumberedSteps() {
        return String.join("\n", numberedSteps);
    }

    /**
     * Gets indent based on nesting level
     *
     * @return 4 spaces repeated based on deepLevel
     */
    private String makeIndent() {
        return "    ".repeat(deepLevel);
    }

    /**
     * Starts the process of transforming scenario to numbered list.
     * <p>Method starts by adding detailed information such as scenario title, list of actor and system actors. Then iterates  through all steps of scenario.</p>
     *
     * @param scenario object {@link Scenario} represents scenario to transform.
     */
    public void visit(Scenario scenario) {
        // Adding base attributes
        numberedSteps.add("Tytuł: " + scenario.title());
        numberedSteps.add("Aktorzy: " + String.join(", ", scenario.actors()));
        numberedSteps.add("Aktorzy systemowi: " + String.join(", ", scenario.systemActors()));
        numberedSteps.add("");

        // Starts processing the scenario
        for (Step step : scenario.scenarios()) {
            step.accept(this);
        }
    }

    /**
     * Distinguishes between steps
     * <p>Method checks the subtype of param and manage it accordingly. Basic steps are being added to numbered list.</p>
     *
     * @param step object {@link Step} represents step to manage.
     */
    public void visit(Step step) {
        if (step.ifStep() != null) {                // IF Step
            visit(step.ifStep());
        } else if (step.forEachStep() != null) {    // FOR EACH Step
            visit(step.forEachStep());
        } else {                                    // BASIC Step
            numberedSteps.add(makeIndent() + currentPrefix + stepCounter + ". " + step.step());
            stepCounter++;
        }
    }

    /**
     * IF Step Coordinator
     * <p>Method firstly iterates by its content, after reaching else it goes back and creates different route. </p>
     *
     * @param ifStep object {@link IfStep} represents If Step to manage.
     */
    public void visit(IfStep ifStep) {

        // Adding IF
        numberedSteps.add(makeIndent() + currentPrefix + stepCounter + ". IF: " + ifStep.condition());

        // Managing Prefixes
        String parentPrefix = currentPrefix;
        currentPrefix = parentPrefix + stepCounter + ".";

        // Go Back to If Number
        int backup = stepCounter;
        stepCounter = 1;

        // Iterates before ELSE
        deepLevel++;
        for (Step step : ifStep.scenario()) {
            step.accept(this);
        }

        // If needed
        if (ifStep.elseStep() != null) {
            deepLevel--;

            // Managing Prefixes after else
            currentPrefix = parentPrefix;
            stepCounter = backup;

            // Adding Else
            numberedSteps.add(makeIndent() + currentPrefix + stepCounter + ". ELSE");

            // Managing Prefixes
            currentPrefix = parentPrefix + stepCounter + ".";
            stepCounter = 1;
            deepLevel++;

            // Iterates after ELSE
            for (Step nestedStep : ifStep.elseStep()) {
                nestedStep.accept(this);
            }
        }

        // Going forward to prefix after IF
        deepLevel--;
        currentPrefix = parentPrefix;
        stepCounter = backup + 1;
    }

    /**
     * For Each Step Coordinator
     * <p>Method iterates by its content, managing all prefixes. </p>
     *
     * @param forEachStep object {@link ForEachStep} represents For Each Step to manage.
     */
    public void visit(ForEachStep forEachStep) {
        // Managing prefixes
        numberedSteps.add(makeIndent() + currentPrefix + stepCounter + ". FOR EACH " + forEachStep.element());
        String parentPrefix = currentPrefix;
        currentPrefix = parentPrefix + stepCounter + ".";

        stepCounter = 1;
        deepLevel++;

        // Iterates by content
        for (Step nestedStep : forEachStep.scenario()) {
            nestedStep.accept(this);
        }

        // Going forward to prefix after For Each
        deepLevel--;
        currentPrefix = parentPrefix;
    }
}
