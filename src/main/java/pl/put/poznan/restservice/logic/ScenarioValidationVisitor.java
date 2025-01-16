package pl.put.poznan.restservice.logic;

import java.util.HashSet;
import java.util.Set;

/**
 * ScenarioValidationVisitor - Validates the correctness of a scenario and its steps.
 * <p>ScenarioValidationVisitor is a concrete implementation of the {@link ScenarioVisitor} interface.
 * It is responsible for traversing a scenario and checking if the steps contain valid actors and system actors.
 * Additionally, it ensures that all steps are described properly and validates any nested "if" and "forEach" structures.</p>
 *
 * <p>This class stores validation errors in a set and provides methods to retrieve those errors.</p>
 *
 * @author Wiktoria Białasik
 * @version 0.1
 */

public class ScenarioValidationVisitor implements ScenarioVisitor {


    private final Set<String> validationErrors = new HashSet<>();
    private final Set<String> actorsUsed = new HashSet<>();
    private final Set<String> systemActorsUsed = new HashSet<>();
    private String[] scenarioActors = new String[0];
    private String[] systemActors = new String[0];

    /**
     * Returns the validation errors encountered during scenario validation.
     *
     * @return a set of validation errors.
     */
    public Set<String> getValidationErrors() {
        return validationErrors;
    }

    /**
     * Validates the scenario by checking for the presence of steps, actors, and system actors.
     * This method processes each step in the scenario and ensures that each defined actor and system actor is used.
     *
     * @param scenario the scenario object to be validated.
     */
    @Override
    public void visit(Scenario scenario) {
        scenarioActors = scenario.actors();
        systemActors = scenario.systemActors();
        if (scenario.scenarios() == null || scenario.scenarios().length == 0) {
            validationErrors.add("The scenario has no steps.");
        }

        for (Step step : scenario.scenarios()) {
            step.accept(this);
        }

        for (String actor : scenarioActors) {
            if (!actorsUsed.contains(actor)) {
                validationErrors.add("Actor not used in the scenario: " + actor);
            }
        }

        for (String systemActor : systemActors) {
            if (!systemActorsUsed.contains(systemActor)) {
                validationErrors.add("System actor not used in the scenario: " + systemActor);
            }
        }
    }

    /**
     * Validates a single step in the scenario. Checks if the step references any defined actors or system actors.
     * Also, validates if the step contains any nested structures such as "if" or "forEach".
     *
     * @param step the step object to be validated.
     */
    @Override
    public void visit(Step step) {

        if (step.step() != null && !step.step().isBlank()) {

            for (String actor : scenarioActors) {
                if (step.step().contains(actor)) {
                    actorsUsed.add(actor);
                }
            }
            for (String systemActor : systemActors) {
                if (step.step().contains(systemActor)) {
                    systemActorsUsed.add(systemActor);
                }
            }
        } else if (step.ifStep() == null && step.forEachStep() == null) {
            validationErrors.add("A step is empty or not described properly.");
        }
        if (step.ifStep() != null) {
            step.ifStep().accept(this);
        } else if (step.forEachStep() != null) {
            step.forEachStep().accept(this);
        }
    }

    /**
     * Validates an "if" step in the scenario. Ensures that the condition is defined and properly described.
     *
     * @param ifStep the "If" object to be validated.
     */
    @Override
    public void visit(IfStep ifStep) {
        if (ifStep.condition() == null || ifStep.condition().isBlank()) {
            validationErrors.add("IfStep condition is missing or empty.");
        }
        if (ifStep.scenario() != null) {
            for (Step step : ifStep.scenario()) {
                step.accept(this);
            }
        }
        if (ifStep.elseStep() != null) {
            for (Step step : ifStep.elseStep()) {
                step.accept(this);
            }
        }
    }

    /**
     * Validates a "forEach" step in the scenario. Ensures that the element is defined and properly described.
     *
     * @param forEachStep the "ForEach" object to be validated.
     */
    @Override
    public void visit(ForEachStep forEachStep) {
        if (forEachStep.element() == null || forEachStep.element().isBlank()) {
            validationErrors.add("ForEachStep element is missing or empty.");
        }
        if (forEachStep.scenario() != null) {
            for (Step step : forEachStep.scenario()) {
                step.accept(this);
            }
        }
    }
}
