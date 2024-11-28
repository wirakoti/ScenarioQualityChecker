package pl.put.poznan.restservice.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *  Klasa odpowiedzialna za tworzenie scenariuszy
 * @author
 */
@Service
public class RestService {

    // Wizytator
    public interface WizytatorScenariusza{
        void visit(Scenariusz scenariusz);
        void visit(Krok krok);
        void visit(IfStep ifStep);
        void visit(ElseStep elseStep);
        void visit(ForEachStep forEachStep);
    }

    public record Scenariusz(String tytul, String[] aktorzy, String aktorSystemowy, Krok[] scenariusz) {
        public void accept(WizytatorScenariusza wizytatorScenariusza) {
            wizytatorScenariusza.visit(this);
            for(Krok krok : scenariusz) {
                krok.accept(wizytatorScenariusza);
            }
        }
    }

    public record Krok(String krok, IfStep ifStep, ElseStep elseStep, ForEachStep forEachStep) {
        public void accept(WizytatorScenariusza wizytatorScenariusza) {
            if(krok != null) {
                wizytatorScenariusza.visit(this);
            }
            if(ifStep != null) {
                ifStep.accept(wizytatorScenariusza);
            }
            if(elseStep != null) {
                elseStep.accept(wizytatorScenariusza);
            }
            if(forEachStep != null) {
                forEachStep.accept(wizytatorScenariusza);
            }
        }
    }

    public record IfStep(String warunek, Krok[] scenariusz) {
        public void accept(WizytatorScenariusza wizytatorScenariusza) {
            wizytatorScenariusza.visit(this);
            for(Krok krok : scenariusz) {
                krok.accept(wizytatorScenariusza);
            }
        }
    }

    public record ElseStep(Krok[] scenariusz) {
        public void accept(WizytatorScenariusza wizytatorScenariusza) {
            wizytatorScenariusza.visit(this);
            for(Krok krok : scenariusz) {
                krok.accept(wizytatorScenariusza);
            }
        }
    }

    public record ForEachStep(String element, Krok[] scenariusz) {
        public void accept(WizytatorScenariusza wizytatorScenariusza) {
            wizytatorScenariusza.visit(this);
            for(Krok krok : scenariusz) {
                krok.accept(wizytatorScenariusza);
            }
        }
    }

    public static class OdwiedzanieWizytatoraScenariusza implements WizytatorScenariusza{

        private List<String> ListaKrokow = new ArrayList<>();

        public List<String> getListaKrokow() {
            return ListaKrokow;
        }

        @Override
        public void visit(Scenariusz scenariusz) {
            ListaKrokow.add("Tytuł: "+scenariusz.tytul());
            ListaKrokow.add("Autorzy: "+String.join(", ",scenariusz.aktorzy()));
            ListaKrokow.add("Aktor Systemowy: "+scenariusz.aktorSystemowy());
        }

        @Override
        public void visit(Krok krok) {
            ListaKrokow.add(krok.krok());
        }

        @Override
        public void visit(IfStep ifStep) {
            ListaKrokow.add("IF: "+ifStep.warunek());
        }

        @Override
        public void visit(ElseStep elseStep) {
            ListaKrokow.add("ELSE: ");
        }

        @Override
        public void visit(ForEachStep forEachStep) {
            ListaKrokow.add("FOR EACH: "+forEachStep.element());
        }

    }

    public Scenariusz PrzetwarzanieScenariusza(String json) throws  Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, Scenariusz.class);
    }

    public String PrzetwarzanieScenariusza(Scenariusz scenariusz, WizytatorScenariusza wizytatorScenariusza) {
        scenariusz.accept(wizytatorScenariusza);

        StringBuilder sb = new StringBuilder();
        if(wizytatorScenariusza instanceof OdwiedzanieWizytatoraScenariusza odwiedzanieWizytatoraScenariusza) {
            for(String krok: odwiedzanieWizytatoraScenariusza.ListaKrokow) {
                sb.append(krok).append("\n");
            }
        }

        return sb.toString();
    }


}

