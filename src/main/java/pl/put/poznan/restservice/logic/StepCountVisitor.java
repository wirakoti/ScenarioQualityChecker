package pl.put.poznan.restservice.logic;

import org.springframework.stereotype.Service;


/**
 * StepCountVisitor - Class description.
 * <p>StepCountVisitor is a concrete implementation of the {@link ScenarioVisitor} interface.
 * It is responsible for traversing a given scenario and counting all steps, including nested ones.
 * The class maintains a single attribute that tracks the total number of steps encountered
 * during the visitation process. This includes steps within "if" and "forEach" constructs.
 * </p>
 *
 * @author Wiktoria Białasik
 * @version 0.1
 */
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Service;

@Service
public class StepCountVisitor implements ScenarioVisitor {

    /**
     * The total count of steps in scenario
     */
    private int stepCount = 0;

    /**
     * Returns the count of steps in the scenario.
     *
     * @return the total step count
     */
    public int getStepCount() {
        return stepCount;
    }

    /**
     * Starts the visitation process for the provided {@link Scenario}.
     * Iterates through all the steps in the scenario and applies the visitor
     * to each step.
     *
     * @param scenario the scenario object to be processed
     */
    public void visit(Scenario scenario) {
        for (Step step : scenario.scenarios()) {
            step.accept(this);
        }
    }

    /**
     * Visits a single {@link Step}. Processes nested structures if present.
     *
     * @param step the step object to be processed
     */
    @Override
    public void visit(Step step) {
        stepCount++;
        if (step.ifStep() != null) {
            step.ifStep().accept(this);
        } else if (step.forEachStep() != null) {
            step.forEachStep().accept(this);
        }
    }

    /**
     * Visits an {@link IfStep}. Processes its main and optional else scenarios.
     *
     * @param ifStep the conditional step
     */
    @Override
    public void visit(IfStep ifStep) {
        for (Step step : ifStep.scenario()) {
            step.accept(this);
        }
        if (ifStep.elseStep() != null) {
            for (Step elseStep : ifStep.elseStep()) {
                elseStep.accept(this);
            }
        }
    }

    /**
     * Visits a {@link ForEachStep}. Processes each nested step.
     *
     * @param forEachStep the iterative step
     */
    @Override
    public void visit(ForEachStep forEachStep) {
        for (Step step : forEachStep.scenario()) {
            step.accept(this);
        }
    }


}
