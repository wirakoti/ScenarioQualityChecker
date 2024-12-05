package pl.put.poznan.restservice.logic;

public record ForEachStep(String element, Step[] scenario) implements ScenarioElement {
    public void accept(ScenarioVisitor scenarioVisitor) {
        scenarioVisitor.visit(this);
        for(Step step : scenario) {
            step.accept(scenarioVisitor);
        }
    }
}