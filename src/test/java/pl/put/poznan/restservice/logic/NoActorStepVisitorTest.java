package pl.put.poznan.restservice.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NoActorStepVisitorTest {

    private NoActorStepVisitor noActorStepVisitor;
    private Scenario mockScenario;
    private Step mockStep;

    @BeforeEach
    void setUp() {
        noActorStepVisitor = new NoActorStepVisitor();
        mockScenario = mock(Scenario.class);
        mockStep = mock(Step.class);
    }

    @Test
    void noActorStepTest() {
        List<String> steps = noActorStepVisitor.getNonActorStep();
        assertNotNull(steps);
        assertTrue(steps.isEmpty());
    }

    @Test
    void validActorStepTest() {
        when(mockScenario.actors()).thenReturn(new String[]{"Actor1", "Actor2"});
        when(mockScenario.systemActors()).thenReturn("SystemActor");
        when(mockScenario.scenarios()).thenReturn(new Step[]{});

        noActorStepVisitor.visit(mockScenario);

        List<String> steps = noActorStepVisitor.getNonActorStep();
        assertTrue(steps.isEmpty());
    }

    @Test
    void allActorStepsTest() {
        when(mockScenario.actors()).thenReturn(new String[]{"Actor1", "Actor2"});
        when(mockScenario.systemActors()).thenReturn("SystemActor");
        when(mockStep.step()).thenReturn("Actor1 does something.");
        when(mockScenario.scenarios()).thenReturn(new Step[]{mockStep});

        noActorStepVisitor.visit(mockScenario);

        List<String> steps = noActorStepVisitor.getNonActorStep();
        assertTrue(steps.isEmpty());
    }

    @Test
    void mixedStepsTest() {
        Step mockStep1 = mock(Step.class);
        Step mockStep2 = mock(Step.class);
        Step mockStep3 = mock(Step.class);

        when(mockScenario.actors()).thenReturn(new String[]{"Sklepikarz", "Kupujący"});
        when(mockScenario.systemActors()).thenReturn("SystemActor");

        when(mockStep1.step()).thenReturn("Sklepikarz sprzedaje chleb/");
        when(mockStep2.step()).thenReturn("Kupujący kupuje.");
        when(mockStep3.step()).thenReturn("Informatyk programuje");
        when(mockScenario.scenarios()).thenReturn(new Step[]{mockStep1, mockStep2, mockStep3});

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

        noActorStepVisitor.visit(mockScenario);
        List<String> steps = noActorStepVisitor.getNonActorStep();

        assertEquals(1, steps.size());
        assertTrue(steps.contains("Informatyk programuje"));
    }

    @Test
    void correctActorsTest() {
        Step mockStep1 = mock(Step.class);
        Step mockStep2 = mock(Step.class);

        when(mockScenario.actors()).thenReturn(new String[]{"Aktorzyna", "Informatyk"});
        when(mockScenario.systemActors()).thenReturn("SystemActor");
        when(mockStep1.step()).thenReturn("Aktorzyna gra.");
        when(mockStep2.step()).thenReturn("Informatyk koduje.");

        when(mockScenario.scenarios()).thenReturn(new Step[]{mockStep1, mockStep2});

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

        noActorStepVisitor.visit(mockScenario);
        List<String> steps = noActorStepVisitor.getNonActorStep();

        assertTrue(steps.isEmpty());
    }


}
