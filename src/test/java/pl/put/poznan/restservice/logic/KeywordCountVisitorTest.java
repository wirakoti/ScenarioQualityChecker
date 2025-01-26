package pl.put.poznan.restservice.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mock;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class KeywordCountVisitorTest {


    @BeforeEach
    void setUp() {


    }

    @Test
    void testEmptyScenario() {
        Scenario mockScenario = mock(Scenario.class);
        when(mockScenario.scenarios()).thenReturn(new Step[]{});

        KeywordCountVisitor keywordCountVisitor = new KeywordCountVisitor();
        keywordCountVisitor.visit(mockScenario);

        assertEquals(0, keywordCountVisitor.getKeywordCount());

    }

    @Test
    void testScenarioWithNoIfOrForEach() {
        Step mockStep = mock(Step.class);
        Scenario mockScenario = mock(Scenario.class);

        when(mockScenario.scenarios()).thenReturn(new Step[]{mockStep});
        when(mockStep.ifStep()).thenReturn(null);
        when(mockStep.forEachStep()).thenReturn(null);

        KeywordCountVisitor keywordCountVisitor = new KeywordCountVisitor();
        keywordCountVisitor.visit(mockScenario);

        verify(mockScenario, times(1)).scenarios();
        verify(mockStep, times(1)).accept(keywordCountVisitor);

        assertEquals(0, keywordCountVisitor.getKeywordCount());  // Should be 0 since there's no "if" or "forEach"
        assertEquals(0, keywordCountVisitor.getIfKeywordCount());  // No "if"
        assertEquals(0, keywordCountVisitor.getForEachKeywordCount());  // No "forEach"
    }


    @Test
    void testScenarioWithIfKeyword() {
        Step mockStep = mock(Step.class);
        Step mockStep2 = mock(Step.class);
        IfStep mockIfStep = mock(IfStep.class);
        Scenario mockScenario = mock(Scenario.class);

        when(mockScenario.scenarios()).thenReturn(new Step[]{mockStep});
        when(mockStep.forEachStep()).thenReturn(null);
        when(mockStep.ifStep()).thenReturn(mockIfStep);
        when(mockIfStep.scenario()).thenReturn(new Step[]{mockStep2});

        doAnswer(invocation -> {
            ScenarioVisitor visitor = invocation.getArgument(0);
            visitor.visit(mockStep);
            return null;
        }).when(mockStep).accept(any(ScenarioVisitor.class));

        KeywordCountVisitor keywordCountVisitor = new KeywordCountVisitor();
        keywordCountVisitor.visit(mockScenario);


        assertEquals(1, keywordCountVisitor.getKeywordCount());  // Should be 1 since there's 1 "if" and no "forEach"
        assertEquals(1, keywordCountVisitor.getIfKeywordCount());  // 1 "if"
        assertEquals(0, keywordCountVisitor.getForEachKeywordCount());  // No "forEach"
    }

    @Test
    void testScenarioWithForEachKeyword() {
        Scenario mockScenario = mock(Scenario.class);
        Step mockStep = mock(Step.class);
        Step mockStep2 = mock(Step.class);
        ForEachStep mockForEach = mock(ForEachStep.class);


        when(mockScenario.scenarios()).thenReturn(new Step[]{mockStep});
        when(mockStep.forEachStep()).thenReturn(mockForEach);
        when(mockStep.ifStep()).thenReturn(null);
        when(mockForEach.scenario()).thenReturn(new Step[]{mockStep2});

        doAnswer(invocation -> {
            ScenarioVisitor visitor = invocation.getArgument(0);
            visitor.visit(mockStep);
            return null;
        }).when(mockStep).accept(any(ScenarioVisitor.class));

        KeywordCountVisitor keywordCountVisitor = new KeywordCountVisitor();
        keywordCountVisitor.visit(mockScenario);


        assertEquals(1, keywordCountVisitor.getKeywordCount());
        assertEquals(0, keywordCountVisitor.getIfKeywordCount());
        assertEquals(1, keywordCountVisitor.getForEachKeywordCount());
    }

    @Test
    void testScenarioWithForEachAndIfKeyword() {
        Scenario mockScenario = mock(Scenario.class);
        Step mockStep = mock(Step.class);
        Step mockStep2 = mock(Step.class);
        IfStep mockIfStep = mock(IfStep.class);
        ForEachStep mockForEach = mock(ForEachStep.class);

        when(mockScenario.scenarios()).thenReturn(new Step[]{mockStep, mockStep2});

        when(mockStep.forEachStep()).thenReturn(mockForEach);
        when(mockStep.ifStep()).thenReturn(null);

        when(mockStep2.forEachStep()).thenReturn(null);
        when(mockStep2.ifStep()).thenReturn(mockIfStep);

        when(mockForEach.scenario()).thenReturn(new Step[]{mock(Step.class)});
        when(mockIfStep.scenario()).thenReturn(new Step[]{mock(Step.class)});


        doAnswer(invocation -> {
            ScenarioVisitor visitor = invocation.getArgument(0);
            visitor.visit(mockStep);
            return null;
        }).when(mockStep).accept(any(ScenarioVisitor.class));

        doAnswer(invocation -> {
            ScenarioVisitor visitor = invocation.getArgument(0);
            visitor.visit(mockStep2);
            return null;
        }).when(mockStep2).accept(any(ScenarioVisitor.class));

        KeywordCountVisitor keywordCountVisitor = new KeywordCountVisitor();
        keywordCountVisitor.visit(mockScenario);


        assertEquals(2, keywordCountVisitor.getKeywordCount());  // Should be 1 since there's 1 "if" and no "forEach"
        assertEquals(1, keywordCountVisitor.getIfKeywordCount());  // 1 "if"
        assertEquals(1, keywordCountVisitor.getForEachKeywordCount());  // No "forEach"
    }

}