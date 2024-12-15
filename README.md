# ScenarioQualityChecker
![example workflow](https://github.com/wirakoti/ScenarioQualityChecker/actions/workflows/maven.yml/badge.svg)

**ScenarioQualityChecker** to narzędzie przeznaczone do oceny i poprawy jakości scenariuszy w **IO Lab**. 

Integruje się z różnymi systemami i interfejsami API w celu dostarczania szczegółowych informacji zwrotnych i zarządzania procesem zapewniania jakości scenariuszy.

## Członkowie zespołu
- **Wiktoria Białasik**
- **Katarzyna Chojniak** (Scrum Master)
- **Mateusz Czechowski** (Proxy Product Owner)
- **Paula Skrzypek**

## Workflow
Narzędzie zapewnia zautomatyzowany workflow, który integruje się z backlogiem produktu oraz baclkogiem sprintu.

## Linki
[Link](https://docs.google.com/spreadsheets/d/1BzeZqAxVHTAzYl7ZYjqGWg1dS7qwtC_Q/edit?usp=sharing&ouid=118269913899199224091&rtpof=true&sd=true) do arkusza z ProductBacklog/SpringBacklog

[Link](https://docs.google.com/spreadsheets/d/e/2PACX-1vTn6j3M8pmGEzrsQk8mXse7lVHUdhYWkfxbkQiYI23rBtwM4N3bWw0qtupW-gesfCkcYasnZ-eEXl-F/pubhtml) do zasad punktowania sprintów

[Link]() do diagramu UML

## Narzędzia
### Użycie `curl`
```bash
curl -X POST http://localhost:8080/RestService/numberedStepList -H "Content-Type: application/json" -d "@input.json"
```

### Użycie `Invoke-WebRequest `
```bash
Invoke-WebRequest -Uri http://localhost:8080/RestService/numberedStepList -Method Post -ContentType "application/json" -InFile "input.json"
```


