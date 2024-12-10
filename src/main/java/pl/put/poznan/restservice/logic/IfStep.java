package pl.put.poznan.restservice.logic;

public record IfStep(String condition, Step[] scenario, ElseStep elseStep) implements ScenarioElement  {
    public void accept(ScenarioVisitor scenarioVisitor) {
        scenarioVisitor.visit(this);
    }
}