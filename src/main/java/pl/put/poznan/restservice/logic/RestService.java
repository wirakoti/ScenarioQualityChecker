package pl.put.poznan.restservice.logic;

import java.util.concurrent.atomic.AtomicLong;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Service
public class RestService {
    public record Scenariusz(String tytul, String[] aktorzy, String aktorSystemowy, Krok[] scenariusz) { }

    public record Krok(String krok, IfStep ifStep, ElseStep elseStep, ForEachStep forEachStep) {}

    public record IfStep(String warunek, Krok[] scenariusz) { }

    public record ElseStep(Krok[] scenariusz) { }

    public record ForEachStep(String element, Krok[] scenariusz) { }

    public Scenariusz PrzetwarzanieScenariusza(String json) throws  Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, Scenariusz.class);
    }

    public String PrzetwarzanieScenariusza(Scenariusz scenariusz) {
        StringBuilder sb = new StringBuilder();
        int i = 1;

        sb.append("Tytuł: ").append(scenariusz.tytul).append("\n");

        return sb.toString();
    }
}
