package pl.put.poznan.restservice.logic;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * NoActorStepVisitor - Class Description
 *
 * <p> The visitor validates in given scenario if how many steps with no keywords does not start with Actor name.</p>
 *
 * @author CzechowskiMateusz
 * @version 1.0
 */

@Service
public class NoActorStepVisitor implements ScenarioVisitor {

    // Class Params
    private final List<String> nonActorStep = new ArrayList<>();
    private List<String> validActors = new ArrayList<>();

    /**
     * Gets final list of steps with no actor at the start.
     *
     * @return list of steps with no actors nor keywords.
     */
    public List<String> getNonActorStep() {
        return nonActorStep;
    }

    /** Checks the validation of current step.
     *
     * @return true if there is actor in list
     */
    private boolean canBeStep(String step){
        if(step == null || step.isBlank()) return false;

        String potentialActor = step.split(" ")[0];
        return validActors.contains(potentialActor);
    }

    /**
     * Starts the process of traversing scenario.
     *
     * @param scenario object {@link Scenario} represents scenario to traverse.
     */
    public void visit(Scenario scenario) {
        this.validActors = new ArrayList<>(List.of(scenario.actors()));
        this.validActors.add(scenario.systemActors());

        for (Step step : scenario.scenarios()) {
            step.accept(this);
        }
    }

    /**
     * Distinguishes between steps
     * <p>Method checks the subtype of param and manage it accordingly, validation of base steps.</p>
     *
     * @param step object {@link Step} represents step to manage.
     */
    public void visit(Step step) {
        if (step.ifStep() != null) {
            visit(step.ifStep());
        } else if (step.forEachStep() != null) {
            visit(step.forEachStep());
        } else {
            if(!canBeStep(step.step())){
                nonActorStep.add(step.step());
            }
        }
    }

    /**
     * IF Step Coordinator
     * <p>Method processes main if and when exists else also this sector.</p>
     *
     * @param ifStep object {@link IfStep} represents If Step to manage.
     */
    public void visit(IfStep ifStep) {
        for(Step step: ifStep.scenario()){
            step.accept(this);
        }
        if(ifStep.elseStep() != null) {
            for(Step step: ifStep.elseStep()){
                step.accept(this);
            }
        }
    }

    /**
     * For Each Step Coordinator
     * <p>Method iterates by its content managing for each step. </p>
     *
     * @param forEachStep object {@link ForEachStep} represents For Each Step to manage.
     */
    public void visit(ForEachStep forEachStep) {
        for(Step step: forEachStep.scenario()){
            step.accept(this);
        }
    }
}
