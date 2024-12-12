package pl.put.poznan.restservice.logic;

import org.springframework.stereotype.Service;


/**
 * KeywordCountVisitor - Class description.
 * <p>KeywordCountVisitor is a concrete implementation of {@link ScenarioVisitor} interface.
 * It is responsible for traversing a given scenario and counting the occurrences of "if" and "forEach" keywords.
 * <br>The class consists of three attributes that store the total count of visited keywords as well as
 * the count of visited "if" and "forEach" keywords.</p>
 *
 *
 * @author Katarzyna Chojniak
 * @version 0.1
 */

@Service
public class KeywordCountVisitor implements ScenarioVisitor {

    /**
     * The total count of all keywords encountered in the scenario
     */
    private int keywordCount = 0;

    /**
     * The count of "if" keywords encountered in the scenario
     */
    private int ifKeywordCount = 0;

    /**
     * The count of "forEach" keywords encountered in the scenario
     */
    private int forEachKeywordCount = 0;

    /**
     * Returns the count of keywords in the scenario.
     *
     * @return the total keyword count
     */
    public int getKeywordCount() {
        return keywordCount;
    }

    /**
     * Returns the count of "if" keywords in the scenario.
     *
     * @return the "if" keyword count
     */
    public int getIfKeywordCount() {
        return ifKeywordCount;
    }

    /**
     * Returns the count of "forEach" keywords in the scenario.
     *
     * @return the "forEach" keyword count
     */
    public int getForEachKeywordCount() {
        return forEachKeywordCount;
    }

    /**
     * Starts the visitation process for the provided {@link Scenario}.
     * Iterates through all the steps in the scenario and applies the visitor
     * to each step.
     *
     * @param scenario the scenario object to be processed
     */
    public void visit(Scenario scenario) {
        // Starts processing the scenario
        for (Step step : scenario.scenarios()) {
            step.accept(this);
        }
    }

    /**
     * Visits a single {@link Step}. If the step contains an "if" or "forEach" class,
     * the corresponding visitation method is called.
     *
     * @param step the step object to be processed
     */
    public void visit(Step step) {
        if (step.ifStep() != null) {                // IF Step
            visit(step.ifStep());
        } else if (step.forEachStep() != null) {    // FOR EACH Step
            visit(step.forEachStep());
        }
    }

    /**
     * Visits an {@link IfStep}, increments the keyword count, and recursively processes
     * any nested steps. If an else step is present, it processes those steps as well.
     *
     * @param ifStep the "If" object to be processed
     */
    public void visit(IfStep ifStep) {
        keywordCount++;
        ifKeywordCount++;

        for (Step step : ifStep.scenario()) {
            step.accept(this);
        }

        if (ifStep.elseStep() != null) {
            for (Step nestedStep : ifStep.elseStep()) {
                nestedStep.accept(this);
            }
        }
    }

    /**
     * Visits a {@link ForEachStep}, increments the keyword count, and recursively processes
     * any nested steps within the "forEach" block.
     *
     * @param forEachStep the ForEachStep object to be processed
     */
    public void visit(ForEachStep forEachStep) {
        keywordCount++;
        forEachKeywordCount++;

        for (Step nestedStep : forEachStep.scenario()) {
            nestedStep.accept(this);
        }
    }
}