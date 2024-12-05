package pl.put.poznan.restservice.logic;

public record Scenario(String title, String[] actors, String systemActors, Step[] scenarios) implements ScenarioElement  {
    public void accept(ScenarioVisitor scenarioVisitor) {
        scenarioVisitor.visit(this);
        for(Step step : scenarios) {
            step.accept(scenarioVisitor);
        }
    }
}