package pl.put.poznan.restservice.logic;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class VisitScenarioVisitor implements  ScenarioVisitor {
    private List<String> stepList = new ArrayList<>();
    ObjectMapper mapper = new ObjectMapper();


    public List<String> getStepList() {
        return stepList;
    }

    public void visit(Scenario scenario) {
        stepList.add("Tytuł: "+scenario.title());
        stepList.add("Autorzy: "+String.join(", ",scenario.actors()));
        stepList.add("Aktor Systemowy: "+scenario.systemActors());
    }

    public void visit(Step step) {
        stepList.add(step.step());
    }

    public void visit(IfStep ifStep) {
        stepList.add("IF: "+ifStep.condition());
    }

    public void visit(ElseStep elseStep) {
        stepList.add("ELSE: ");
    }

    public void visit(ForEachStep forEachStep) {
        stepList.add("FOR EACH " + forEachStep.element() + ":");
    }
}
