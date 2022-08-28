package controllers.testControllers;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users/{id}/statistic")
public class TestController {
    public Map<String, Object> getStatistic(@PathVariable String id) {
        return new HashMap<>(Map.of("name", (id.equals("john") ? "John" : "Emma")));
    }
}
