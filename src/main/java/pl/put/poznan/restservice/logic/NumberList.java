package pl.put.poznan.restservice.logic;

import org.springframework.stereotype.Service;

import javax.script.ScriptEngineManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@Service
public class NumberList implements ScenarioVisitor {

    private final List<String> numberedSteps = new ArrayList<>();
    private final Stack<Integer> stepStack = new Stack<>();
    private int stepCounter = 1;
    private String currentPrefix = "";

    /**
     * Zwraca numerowaną listę kroków.
     *
     * @return Numerowana lista kroków w formie tekstu.
     */
    public String getNumberedSteps() {
        return String.join("\n", numberedSteps);
    }

    public void visit(Scenario scenario) {
        numberedSteps.add("Tytuł: " + scenario.title());
        numberedSteps.add("Aktorzy: " + String.join(", ", scenario.actors()));
        numberedSteps.add("Aktorzy systemowi: " + String.join(", ", scenario.systemActors()));
        numberedSteps.add("");

        for (Step step : scenario.scenarios()) {
            step.accept(this);
        }
    }

    public void visit(Step step) {

            if (step.ifStep() != null) {
                visit(step.ifStep());
            } else if (step.forEachStep() != null) {
                visit(step.forEachStep());
            } else {
                numberedSteps.add(currentPrefix + stepCounter + ". " + step.step());
                stepCounter++;
            }

    }

    public void visit(IfStep ifStep) {

        numberedSteps.add(currentPrefix + stepCounter + ". IF: " + ifStep.condition());
        String parentPrefix = currentPrefix;
        currentPrefix = parentPrefix + stepCounter + ".";

        stepCounter = 1;

        for (Step nestedStep : ifStep.scenario()) {
            nestedStep.accept(this);
        }

        if (ifStep.elseStep() != null) {
            visit(ifStep.elseStep()); // Visit ELSE step
        }

        currentPrefix = parentPrefix;
    }

    public void visit(ElseStep elseStep) {
        numberedSteps.add(currentPrefix + stepCounter + " ELSE");
        String parentPrefix = currentPrefix;
        currentPrefix = parentPrefix + stepCounter + ".";

        stepCounter = 1;

        for (Step nestedStep : elseStep.scenario()) {
            nestedStep.accept(this);
        }

        currentPrefix = parentPrefix;
    }

    public void visit(ForEachStep forEachStep) {
        numberedSteps.add(currentPrefix + stepCounter + ". FOR EACH " + forEachStep.element());
        String parentPrefix = currentPrefix;
        currentPrefix = parentPrefix + stepCounter + ".";

        stepCounter = 1;

        for (Step nestedStep : forEachStep.scenario()) {
            nestedStep.accept(this);
        }

        currentPrefix = parentPrefix;
    }
}
