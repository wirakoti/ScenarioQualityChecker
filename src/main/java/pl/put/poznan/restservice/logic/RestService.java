package pl.put.poznan.restservice.logic;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

public class RestService {
    public record Scenariusz(String tytul, String[] aktorzy, String aktorSystemowy, Krok[] scenariusz) { }

    public record Krok(String krok, IfStep ifStep, ElseStep elseStep, ForEachStep forEachStep) {}

    public record IfStep(String warunek, Krok[] scenariusz) { }

    public record ElseStep(Krok[] scenariusz) { }

    public record ForEachStep(String element, Krok[] scenariusz) { }
}
