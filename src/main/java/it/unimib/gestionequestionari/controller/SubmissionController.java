package it.unimib.gestionequestionari.controller;

import it.unimib.gestionequestionari.model.QuestionnaireSubmission;
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

    @GetMapping("/fill/{questionnaireId}")
    public String startForm(@PathVariable Long questionnaireId, Model model) {
        model.addAttribute("questionnaireId", questionnaireId);
        return "submissions/start";
    }

    @PostMapping("/fill/{questionnaireId}")
    public String start(@PathVariable Long questionnaireId,
                        @RequestParam("email") String email) {

        QuestionnaireSubmission submission = submissionService.createDraft(questionnaireId, email);
        return "redirect:/submissions/" + submission.getAccessCode();
    }

    @GetMapping("/submissions/access")
    public String accessByCode(@RequestParam("code") String code) {
        return "redirect:/submissions/" + code.trim();
    }

    @GetMapping("/submissions/{code}")
    public String view(@PathVariable String code, Model model) {
        try {
            QuestionnaireSubmission submission = submissionService.findByCode(code);
            model.addAttribute("submission", submission);
            model.addAttribute("questionnaire", submission.getQuestionnaire());
            return "submissions/fill";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("code", code);
            return "submissions/not-found";
        }
    }

    @PostMapping("/submissions/{code}/save")
    public String saveDraft(@PathVariable String code,
                            @RequestParam Map<String, String> params) {

        submissionService.saveDraft(code, extractAnswers(params));
        return "redirect:/submissions/" + code;
    }

    @PostMapping("/submissions/{code}/submit")
    public String submitFinal(@PathVariable String code,
                              @RequestParam Map<String, String> params) {

        submissionService.submitFinal(code, extractAnswers(params));
        return "redirect:/submissions/" + code + "?final=true";
    }

    @PostMapping("/submissions/{code}/delete")
    public String delete(@PathVariable String code) {
        submissionService.deleteByCode(code);
        return "redirect:/?deleted=true";
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