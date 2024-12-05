package pl.put.poznan.restservice.logic;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NumberList implements ScenarioElement {

    private final List<String> numberedSteps = new ArrayList<>();
    private int stepCounter = 1; // Zmienna odpowiedzialna za numerację kroków

    /**
     * Zwraca numerowaną listę kroków.
     *
     * @return Numerowana lista kroków w formie tekstu.
     */
    public String getNumberedSteps() {
        return String.join("\n", numberedSteps);
    }

    @Override
    public void accept(ScenarioVisitor scenarioVisitor) {
        if (scenarioVisitor instanceof VisitScenarioVisitor visitScenarioVisitor) {
            for (String step : visitScenarioVisitor.getStepList()) {
                numberedSteps.add(stepCounter + ". " + step);
                System.out.println(stepCounter + ". " + step);
                stepCounter++;
            }
        } else {
            throw new IllegalArgumentException("Unsupported visitor type for NumberList");
        }
    }
}
