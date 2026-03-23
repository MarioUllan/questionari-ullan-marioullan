package it.unimib.gestionequestionari.controller;

import it.unimib.gestionequestionari.model.Submission;
import it.unimib.gestionequestionari.service.SubmissionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
public class SubmissionController {

    private final SubmissionService submissionService;

    public SubmissionController(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    /**
     * Start page: enter questionnaire access code + email.
     */
    @GetMapping("/fill")
    public String startForm() {
        return "submissions/start";
    }

    @PostMapping("/fill")
    public String start(@RequestParam("accessCode") String accessCode,
                        @RequestParam("email") String email,
                        Model model) {
        try {
            Submission submission = submissionService.startOrResume(accessCode, email);
            return "redirect:/submissions/" + submission.getId();
        } catch (RuntimeException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("accessCode", accessCode);
            model.addAttribute("email", email);
            return "submissions/start";
        }
    }

    @GetMapping("/submissions/{id}")
    public String view(@PathVariable Long id, Model model) {
        try {
            Submission submission = submissionService.findById(id);
            model.addAttribute("submission", submission);
            model.addAttribute("questionnaire", submission.getQuestionnaire());
            return "submissions/fill";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("id", id);
            return "submissions/not-found";
        }
    }

    @PostMapping("/submissions/{id}/save")
    public String saveDraft(@PathVariable Long id,
                            @RequestParam Map<String, String> params) {
        submissionService.saveDraft(id, extractAnswers(params));
        return "redirect:/submissions/" + id;
    }

    @PostMapping("/submissions/{id}/submit")
    public String submit(@PathVariable Long id,
                         @RequestParam Map<String, String> params,
                         Model model) {
        try {
            submissionService.submit(id, extractAnswers(params));
            return "redirect:/submissions/" + id + "?submitted=true";
        } catch (RuntimeException ex) {
            // re-render with error
            Submission submission = submissionService.findById(id);
            model.addAttribute("submission", submission);
            model.addAttribute("questionnaire", submission.getQuestionnaire());
            model.addAttribute("error", ex.getMessage());
            return "submissions/fill";
        }
    }

    private Map<Long, String> extractAnswers(Map<String, String> params) {
        Map<Long, String> values = new HashMap<>();
        for (var e : params.entrySet()) {
            String k = e.getKey();
            if (k.startsWith("answer_")) {
                Long qId = Long.valueOf(k.substring("answer_".length()));
                values.put(qId, e.getValue());
            }
        }
        return values;
    }
}
