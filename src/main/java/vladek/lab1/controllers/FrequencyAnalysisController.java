package vladek.lab1.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vladek.lab1.services.IFrequencyAnalysisService;

import java.util.HashMap;

@RestController
@RequestMapping("/api/lab1/freq-analysis")
public class FrequencyAnalysisController {
    @Autowired
    IFrequencyAnalysisService frequencyAnalysisService;

    @GetMapping("/calculate")
    public ResponseEntity<HashMap<String, Integer>> calculateFrequencies(@RequestParam String text) {
        HashMap<String, Integer> result = frequencyAnalysisService.calculateFrequencies(text);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
