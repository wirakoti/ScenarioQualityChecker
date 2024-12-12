package pl.put.poznan.restservice.logic;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class ScenarioProccesor {
    public Scenario Proccesing(String json) throws Exception{
        ObjectMapper mapper = new ObjectMapper();

        return mapper.readValue(json, Scenario.class);
    }

    public <T> String Parsing(T object) throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
}
