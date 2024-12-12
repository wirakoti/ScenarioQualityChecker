package pl.put.poznan.restservice.logic;

public record Step(String step, IfStep ifStep, ForEachStep forEachStep) implements ScenarioElement  {
    public void accept(ScenarioVisitor scenarioVisitor) {
        scenarioVisitor.visit(this);
    }
}