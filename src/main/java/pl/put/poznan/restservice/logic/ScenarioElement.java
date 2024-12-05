package pl.put.poznan.restservice.logic;

public interface ScenarioElement {
    void accept(ScenarioVisitor scenarioVisitor);
}

