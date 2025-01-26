package pl.put.poznan.restservice.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ScenarioValidationVisitorTest {

    private ScenarioValidationVisitor validationVisitor;
    private Scenario mockScenario;
    private Step mockStep;

    @BeforeEach
    void setUp() {
        validationVisitor = new ScenarioValidationVisitor();
        mockScenario = mock(Scenario.class);
        mockStep = mock(Step.class);
    }

    @Test
    void scenarioWithNoStepsTest() {
        when(mockScenario.actors()).thenReturn(new String[]{});
        when(mockScenario.systemActors()).thenReturn("");
        when(mockScenario.scenarios()).thenReturn(new Step[]{});

        validationVisitor.visit(mockScenario);

        Set<String> errors = validationVisitor.getValidationErrors();
        assertTrue(errors.contains("The scenario has no steps."));
        assertEquals(1, errors.size());
    }

    @Test
    void actorNotUsedInScenarioTest() {
        when(mockScenario.actors()).thenReturn(new String[]{"Actor1"});
        when(mockScenario.systemActors()).thenReturn("");
        when(mockScenario.scenarios()).thenReturn(new Step[]{mockStep});

        when(mockStep.step()).thenReturn("Unrelated action.");

        // Ensure the Step accept method calls the visitor
        doAnswer(invocation -> {
            ScenarioVisitor visitor = invocation.getArgument(0);
            visitor.visit(mockStep);
            return null;
        }).when(mockStep).accept(any(ScenarioVisitor.class));

        validationVisitor.visit(mockScenario);

        Set<String> errors = validationVisitor.getValidationErrors();
        assertTrue(errors.contains("Actor not used in the scenario: Actor1"));
        assertEquals(1, errors.size());
    }

    @Test
    void systemActorNotUsedTest() {
        when(mockScenario.actors()).thenReturn(new String[]{"Actor1"});
        when(mockScenario.systemActors()).thenReturn("SystemActor");
        when(mockScenario.scenarios()).thenReturn(new Step[]{mockStep});

        when(mockStep.step()).thenReturn("Actor1 does something.");

        // Ensure the Step accept method calls the visitor
        doAnswer(invocation -> {
            ScenarioVisitor visitor = invocation.getArgument(0);
            visitor.visit(mockStep);
            return null;
        }).when(mockStep).accept(any(ScenarioVisitor.class));

        validationVisitor.visit(mockScenario);

        Set<String> errors = validationVisitor.getValidationErrors();
        assertTrue(errors.contains("System actor not used in the scenario: SystemActor"));
    }

    @Test
    void validScenarioTest() {
        Step mockStep1 = mock(Step.class);
        Step mockStep2 = mock(Step.class);

        when(mockScenario.actors()).thenReturn(new String[]{"Actor1"});
        when(mockScenario.systemActors()).thenReturn("SystemActor");

        when(mockStep1.step()).thenReturn("Actor1 performs an action.");
        when(mockStep2.step()).thenReturn("SystemActor performs some action.");

        when(mockScenario.scenarios()).thenReturn(new Step[]{mockStep1, mockStep2});

        // Ensure both steps are visited correctly
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

        validationVisitor.visit(mockScenario);

        Set<String> errors = validationVisitor.getValidationErrors();

        assertTrue(errors.isEmpty());
    }
    @Test
    void multipleActorsNotUsedTest() {
        Step mockStep1 = mock(Step.class);
        Step mockStep2 = mock(Step.class);

        when(mockScenario.actors()).thenReturn(new String[]{"Actor1", "Actor2"});
        when(mockScenario.systemActors()).thenReturn("");
        when(mockScenario.scenarios()).thenReturn(new Step[]{mockStep1, mockStep2});

        when(mockStep1.step()).thenReturn("Unrelated action.");
        when(mockStep2.step()).thenReturn("Unrelated action.");

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

        validationVisitor.visit(mockScenario);

        Set<String> errors = validationVisitor.getValidationErrors();

        assertTrue(errors.contains("Actor not used in the scenario: Actor1"));
        assertTrue(errors.contains("Actor not used in the scenario: Actor2"));
        assertEquals(2, errors.size());
    }
    @Test
    void invalidStepDescriptionTest() {
        Step mockStep1 = mock(Step.class);
        Step mockStep2 = mock(Step.class);

        when(mockScenario.actors()).thenReturn(new String[]{"Actor1"});
        when(mockScenario.systemActors()).thenReturn("");
        when(mockScenario.scenarios()).thenReturn(new Step[]{mockStep1, mockStep2});

        when(mockStep1.step()).thenReturn("");
        when(mockStep2.step()).thenReturn("Actor1 performs action.");

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

        validationVisitor.visit(mockScenario);

        Set<String> errors = validationVisitor.getValidationErrors();

        assertTrue(errors.contains("A step is empty or not described properly."));
        assertEquals(1, errors.size());
    }

}
