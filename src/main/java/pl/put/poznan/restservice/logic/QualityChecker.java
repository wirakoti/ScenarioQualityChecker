package pl.put.poznan.restservice.logic;

public class QualityChecker {
    private float scenarioQuality;
    private Scenario scenario;

    public QualityChecker(Scenario scenario) {
        this.scenario = scenario;
        this.scenarioQuality = calculateScenarioQuality();
    }

    public float getScenarioQuality() {
        return scenarioQuality;
    }

    private float calculateScenarioQuality() {
        float stepCount = getStepCount();
        float keywordCount = getKeywordCount();

        return 1 - (keywordCount / stepCount);
    }

    private float getStepCount() {
        StepCountVisitor stepCountVisitor = new StepCountVisitor();
        scenario.accept(stepCountVisitor);

        return stepCountVisitor.getStepCount();
    }

    private float getKeywordCount() {
        KeywordCountVisitor keywordCountVisitor = new KeywordCountVisitor();
        scenario.accept(keywordCountVisitor);
        int keywordCount = keywordCountVisitor.getKeywordCount();
        int ifKeywordCount = keywordCountVisitor.getIfKeywordCount();
        int forEachKeywordCount = keywordCountVisitor.getForEachKeywordCount();

        return keywordCount;
    }

}
