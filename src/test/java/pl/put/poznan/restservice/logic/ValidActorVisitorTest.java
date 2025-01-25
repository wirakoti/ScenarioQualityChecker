package pl.put.poznan.restservice.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ValidActorVisitorTest {

    private ValidActorVisitor validActorVisitor;
    private Scenario mockScenario;
    private Step mockStep1;
    private Step mockStep2;
    private Step mockStep3;



    @BeforeEach
    void setUp() {
        validActorVisitor = new ValidActorVisitor();
        mockScenario = mock(Scenario.class);
        mockStep1 = mock(Step.class);
        mockStep2 = mock(Step.class);
        mockStep3 = mock(Step.class);
    }

    @Test
    void getEmptyActorsTest() {
        List<String> actors = validActorVisitor.getActors();
        assertNotNull(actors);
        assertTrue(actors.isEmpty());
    }

    @Test
    void validateActorsTest() {
        when(mockScenario.actors()).thenReturn(new String[]{"Bibliotekarz", "Księgowy"});
        when(mockScenario.systemActors()).thenReturn("SystemActor");
        when(mockScenario.scenarios()).thenReturn(new Step[]{});

        validActorVisitor.visit(mockScenario);

        List<String> actors = validActorVisitor.getActors();
        assertTrue(actors.isEmpty());
    }

    @Test
    void verifyActorsTest() {
        when(mockScenario.actors()).thenReturn(new String[]{"Aktor", "Widz"});
        when(mockScenario.systemActors()).thenReturn("SystemActor");
        when(mockScenario.scenarios()).thenReturn(new Step[]{});

        validActorVisitor.visit(mockScenario);

        verify(mockScenario, times(1)).actors();
        verify(mockScenario, times(1)).systemActors();
        verify(mockScenario, times(1)).scenarios();
    }

    @Test
    void isActorTest(){
        validActorVisitor.validActors  = List.of("Sklepikarz", "Malarz");

        // Poprawne
        assertTrue(validActorVisitor.isActor("Sklepikarz"));
        assertTrue(validActorVisitor.isActor("Malarz"));

        // Niepoprawne
        assertFalse(validActorVisitor.isActor("Informatyk"));
    }

    @Test
    void visitScenarioTest() {
        when(mockScenario.actors()).thenReturn(new String[]{"Actor1", "Actor2"});
        when(mockScenario.systemActors()).thenReturn("SystemActor");

        when(mockStep1.step()).thenReturn("Actor1 step 1");
        when(mockStep2.step()).thenReturn("Actor2 step 2");
        when(mockStep3.step()).thenReturn("Actor3 step 3");

        when(mockScenario.scenarios()).thenReturn(new Step[]{mockStep1, mockStep2, mockStep3});

        validActorVisitor.visit(mockScenario);

        List<String> actorsList = validActorVisitor.getActors();
        assertFalse(actorsList.contains("Actor1"));
    }

}
