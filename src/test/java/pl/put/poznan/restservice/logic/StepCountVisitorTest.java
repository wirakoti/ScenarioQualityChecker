package pl.put.poznan.restservice.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StepCountVisitorTest {

    private StepCountVisitor stepCountVisitor;
    private Scenario mockScenario;
    private Step mockStep;

    @BeforeEach
    void setUp() {
        stepCountVisitor = new StepCountVisitor();
        mockScenario = mock(Scenario.class);
        mockStep = mock(Step.class);
    }

    @Test
    void singleStepTest() {
        when(mockScenario.scenarios()).thenReturn(new Step[]{mockStep});
        when(mockStep.step()).thenReturn("Actor1 performs an action.");

        doAnswer(invocation -> {
            ScenarioVisitor visitor = invocation.getArgument(0);
            visitor.visit(mockStep);
            return null;
        }).when(mockStep).accept(any(ScenarioVisitor.class));

        stepCountVisitor.visit(mockScenario);

        int stepCount = stepCountVisitor.getStepCount();
        assertEquals(1, stepCount);
    }

    @Test
    void emptyScenarioTest() {

        when(mockScenario.scenarios()).thenReturn(new Step[]{});

        stepCountVisitor.visit(mockScenario);

        int stepCount = stepCountVisitor.getStepCount();
        assertEquals(0, stepCount);
    }

    @Test
    void scenarioWithMultipleStepsTest() {
        Step mockStep1 = mock(Step.class);
        Step mockStep2 = mock(Step.class);

        when(mockScenario.scenarios()).thenReturn(new Step[]{mockStep1, mockStep2});
        when(mockStep1.step()).thenReturn("Actor1 performs an action.");
        when(mockStep2.step()).thenReturn("Actor2 performs another action.");

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

        stepCountVisitor.visit(mockScenario);

        int stepCount = stepCountVisitor.getStepCount();
        assertEquals(2, stepCount);
    }

}
