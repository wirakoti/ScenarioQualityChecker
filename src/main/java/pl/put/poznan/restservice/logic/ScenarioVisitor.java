package pl.put.poznan.restservice.logic;

public interface ScenarioVisitor{
    void visit(Scenario scenario);
    void visit(Step step);
    void visit(IfStep ifStep);
    void visit(ElseStep elseStep);
    void visit(ForEachStep forEachStep);
}