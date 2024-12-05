package pl.put.poznan.restservice.logic;

import org.springframework.stereotype.Service;

@Service
public class KeywordCountVisitor implements ScenarioVisitor {
    private int keywordCount = 0;

    public int getKeywordCount() {
        return keywordCount;
    }

    public void visit(Scenario scenario) {
        ;
    }

    public void visit(Step step) {
        ;
    }

    public void visit(IfStep ifStep) {
        keywordCount++;
    }

    public void visit(ElseStep elseStep) {
        keywordCount++;
    }

    public void visit(ForEachStep forEachStep) {
        keywordCount++;
    }
}