package pl.put.poznan.restservice.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DuplicateStepCheckVisitorTest {

    private DuplicateStepCheckVisitor duplicateStepCheckVisitor;
    private Scenario mockScenario;
    private Step mockStep;

    @BeforeEach
    void setUp() {
        duplicateStepCheckVisitor = new DuplicateStepCheckVisitor();
        mockScenario = mock(Scenario.class);
        mockStep = mock(Step.class);
    }

    @Test
    void noDuplicateStepsTest() {
        when(mockScenario.scenarios()).thenReturn(new Step[]{mockStep});
        when(mockStep.step()).thenReturn("Actor1 performs an action.");

        doAnswer(invocation -> {
            ScenarioVisitor visitor = invocation.getArgument(0);
            visitor.visit(mockStep);
            return null;
        }).when(mockStep).accept(any(ScenarioVisitor.class));

        duplicateStepCheckVisitor.visit(mockScenario);

        String duplicateStepsMessage = duplicateStepCheckVisitor.getDuplicateSteps();
        assertEquals("No duplicate steps found in the scenario.", duplicateStepsMessage);
    }

    @Test
    void singleDuplicateStepTest() {
        Step mockStep1 = mock(Step.class);
        Step mockStep2 = mock(Step.class);

        when(mockScenario.scenarios()).thenReturn(new Step[]{mockStep1, mockStep2});
        when(mockStep1.step()).thenReturn("Actor1 performs an action.");
        when(mockStep2.step()).thenReturn("Actor1 performs an action.");

        doAnswer(invocation -> {
            ScenarioVisitor visitor = invocation.getArgument(0);
            visitor.visit(mockStep1);
            return null;
        }).when(mockStep1).accept(any(ScenarioVisitor.class));

        doAnswer(invocation -> {
            ScenarioVisitor visitor = invocation.getArgument(0);
            visitor.visit(mockStep2);
            return null;
        }).when(mockStep2).accept(any(ScenarioVisitor.class));

        duplicateStepCheckVisitor.visit(mockScenario);

        String duplicateStepsMessage = duplicateStepCheckVisitor.getDuplicateSteps();
        assertTrue(duplicateStepsMessage.contains("Duplicate steps found: [Actor1 performs an action.]"));
    }

    @Test
    void multipleDuplicateStepsTest() {
        Step mockStep1 = mock(Step.class);
        Step mockStep2 = mock(Step.class);
        Step mockStep3 = mock(Step.class);

        when(mockScenario.scenarios()).thenReturn(new Step[]{mockStep1, mockStep2, mockStep3});
        when(mockStep1.step()).thenReturn("Actor1 performs an action.");
        when(mockStep2.step()).thenReturn("Actor1 performs an action.");
        when(mockStep3.step()).thenReturn("Actor1 performs another action.");

        doAnswer(invocation -> {
            ScenarioVisitor visitor = invocation.getArgument(0);
            visitor.visit(mockStep1);
            return null;
        }).when(mockStep1).accept(any(ScenarioVisitor.class));

        doAnswer(invocation -> {
            ScenarioVisitor visitor = invocation.getArgument(0);
            visitor.visit(mockStep2);
            return null;
        }).when(mockStep2).accept(any(ScenarioVisitor.class));

        doAnswer(invocation -> {
            ScenarioVisitor visitor = invocation.getArgument(0);
            visitor.visit(mockStep3);
            return null;
        }).when(mockStep3).accept(any(ScenarioVisitor.class));

        duplicateStepCheckVisitor.visit(mockScenario);

        String duplicateStepsMessage = duplicateStepCheckVisitor.getDuplicateSteps();
        System.out.println(duplicateStepsMessage);
        assertTrue(duplicateStepsMessage.contains("Duplicate steps found: [Actor1 performs an action.]"));
    }

}
