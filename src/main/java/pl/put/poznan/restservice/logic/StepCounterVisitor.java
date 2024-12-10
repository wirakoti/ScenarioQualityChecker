package pl.put.poznan.restservice.logic;

import org.springframework.stereotype.Service;

@Service
public class StepCounterVisitor implements ScenarioVisitor {

    private int stepCount = 0;

    public int getStepCount() {
        return stepCount;
    }

    @Override
    public void visit(Scenario scenario) {

    }

    @Override
    public void visit(Step step) {
        stepCount++;
    }

    @Override
    public void visit(IfStep ifStep) {
        stepCount++;
    }

    @Override
    public void visit(ElseStep elseStep) {
        stepCount++;
    }

    @Override
    public void visit(ForEachStep forEachStep) {
        stepCount++;
    }
}
