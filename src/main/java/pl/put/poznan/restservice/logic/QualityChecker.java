package pl.put.poznan.restservice.logic;

public class QualityChecker {
    private double scenarioQuality;
    private Scenario scenario;

    public QualityChecker(Scenario scenario) {
        this.scenario = scenario;
        this.scenarioQuality = calculateScenarioQuality();
    }

    public double getScenarioQuality() {
        return scenarioQuality;
    }

    private double calculateScenarioQuality() {
        double qualityResult = 1;

        float stepCount = getStepCount();
        float keywordCount = getKeywordCount();
        float ifCount = getIfKeywordCount();

        if (ifCount < Math.log(stepCount)) {
            qualityResult /= (Math.log(stepCount) - ifCount);
        }

        if (keywordCount < 1) {
            qualityResult *= 0.5;
        }

        float noActorStepCount = getNoActorStepCount();
        float potentialActorCount = getValidActorCount();

        float actorValue = (noActorStepCount - potentialActorCount) / stepCount;

        if (noActorStepCount - potentialActorCount > 0) {
            qualityResult *= 1 - actorValue;
        }

        return qualityResult;
    }

    private float getStepCount() {
        StepCountVisitor stepCountVisitor = new StepCountVisitor();
        scenario.accept(stepCountVisitor);

        return stepCountVisitor.getStepCount();
    }

    private float getKeywordCount() {
        KeywordCountVisitor keywordCountVisitor = new KeywordCountVisitor();
        scenario.accept(keywordCountVisitor);

        return keywordCountVisitor.getKeywordCount();
    }

    private float getIfKeywordCount() {
        KeywordCountVisitor keywordCountVisitor = new KeywordCountVisitor();
        scenario.accept(keywordCountVisitor);

        return keywordCountVisitor.getIfKeywordCount();
    }

    private float getNoActorStepCount() {
        NoActorStepVisitor noActorStepVisitor = new NoActorStepVisitor();
        scenario.accept(noActorStepVisitor);
        return noActorStepVisitor.getNonActorStep().size();
    }

    private float getValidActorCount() {
        ValidActorVisitor validActorVisitor = new ValidActorVisitor();
        scenario.accept(validActorVisitor);
        return validActorVisitor.getActors().size();
    }

}
