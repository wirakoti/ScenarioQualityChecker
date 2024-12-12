package pl.put.poznan.restservice.logic;

public record IfStep(String condition, Step[] scenario, Step[] elseStep) implements ScenarioElement  {
    public void accept(ScenarioVisitor scenarioVisitor) {
        scenarioVisitor.visit(this);
    }
}