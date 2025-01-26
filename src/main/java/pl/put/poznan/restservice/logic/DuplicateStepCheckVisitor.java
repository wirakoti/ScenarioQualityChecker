package pl.put.poznan.restservice.logic;

import java.util.HashSet;
import java.util.Set;
import org.springframework.stereotype.Service;

/**
 * DuplicateStepCheckVisitor - Class description.
 * <p>DuplicateStepCheckVisitor is a concrete implementation of {@link ScenarioVisitor} interface.
 * It is responsible for traversing a given scenario and checking for duplicate steps.
 * The class maintains two sets: one for unique steps and another for duplicate steps.
 * <br>When a duplicate step is found, it is added to the duplicate steps set.</p>
 *
 * @author Wiktoria Białasik
 * @version 0.1
 */
@Service
public class DuplicateStepCheckVisitor implements ScenarioVisitor {

    private final Set<String> uniqueSteps = new HashSet<>();
    private final Set<String> duplicateSteps = new HashSet<>();

    /**
     * Returns a string message indicating whether any duplicate steps were found.
     *
     * @return a formatted message about duplicate steps
     */
    public String getDuplicateSteps() {
        if (!duplicateSteps.isEmpty()) {
            return "Duplicate steps found: " + duplicateSteps;
        } else {
            return "No duplicate steps found in the scenario.";
        }
    }

    /**
     * Starts the visitation process for the provided {@link Scenario}.
     * Iterates through all the steps in the scenario and applies the visitor
     * to each step.
     *
     * @param scenario the scenario object to be processed
     */
    @Override
    public void visit(Scenario scenario) {
        // Starts processing the scenario
        for (Step step : scenario.scenarios()) {
            step.accept(this);
        }
    }

    /**
     * Visits a single {@link Step}. If the step contains a valid description,
     * it checks for duplicates by adding it to the uniqueSteps set. If the
     * step is already in the set, it is added to the duplicateSteps set.
     * @param step the step object to be processed
     */
    @Override
    public void visit(Step step) {

        if (step.step() != null && !uniqueSteps.add(step.step())) {
            duplicateSteps.add(step.step());
        }

        if (step.ifStep() != null) {
            step.ifStep().accept(this);
        } else if (step.forEachStep() != null) {
            step.forEachStep().accept(this);
        }
    }

    /**
     * Visits an {@link IfStep}. It recursively processes all steps inside the "if" block.
     * If an else block is present, it processes all the steps within that block as well.
     *
     * @param ifStep the IfStep object to be processed
     */
    @Override
    public void visit(IfStep ifStep) {
        // Process the steps inside the "if" block
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
     * Visits a {@link ForEachStep}. It recursively processes all the steps inside the "forEach" block.
     *
     * @param forEachStep the ForEachStep object to be processed
     */
    @Override
    public void visit(ForEachStep forEachStep) {
        // Process the steps inside the "forEach" block
        for (Step step : forEachStep.scenario()) {
            step.accept(this);
        }
    }
}
