package pl.put.poznan.restservice.logic;

public record ElseStep(Step[] scenario) implements ScenarioElement {
    public void accept(ScenarioVisitor scenarioVisitor) {
        scenarioVisitor.visit(this);
    }
}