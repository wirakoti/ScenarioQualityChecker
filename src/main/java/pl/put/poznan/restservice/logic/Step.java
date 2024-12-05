package pl.put.poznan.restservice.logic;

public record Step(String step, IfStep ifStep, ElseStep elseStep, ForEachStep forEachStep) implements ScenarioElement  {
    public void accept(ScenarioVisitor scenarioVisitor) {
        if(step != null) {
            scenarioVisitor.visit(this);
        }
        if(ifStep != null) {
            ifStep.accept(scenarioVisitor);
        }
        if(elseStep != null) {
            elseStep.accept(scenarioVisitor);
        }
        if(forEachStep != null) {
            forEachStep.accept(scenarioVisitor);
        }
    }
}