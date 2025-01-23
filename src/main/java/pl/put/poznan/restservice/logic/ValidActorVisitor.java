package pl.put.poznan.restservice.logic;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * ValidActorVisitor - Class Description
 *
 * <p>Class used for checking all steps of scenario for potentially missing actors in definition of JSON file.</p>
 *
 * @author CzechowskiMateusz
 * @version 1.0
 */

@Service
public class ValidActorVisitor implements ScenarioVisitor {

    // Class Params
    private final List<String> potentialActors = new ArrayList<>();
    private List<String> validActors = new ArrayList<>();

    /**
     * Gets final list of potential actors.
     *
     * @return list of potential actors.
     */
    public List<String> getActors() {
        return potentialActors;
    }

    /** Checks the validation of current step.
     *
     * @return true if there is an  actor in list
     */
    private boolean isActor(String step){
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
            if(!isActor(step.step())){
                if(!validActors.contains(step.step()))
                    validActors.add(step.step().split(" ")[0]);

                potentialActors.add(step.step().split(" ")[0]);
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
