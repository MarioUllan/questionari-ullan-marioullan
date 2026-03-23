package it.unimib.gestionequestionari.controller;

import it.unimib.gestionequestionari.service.QuestionService;
import it.unimib.gestionequestionari.service.QuestionnaireService;
import it.unimib.gestionequestionari.service.SubmissionService;
import it.unimib.gestionequestionari.model.Questionnaire;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class QuestionnaireController {

    private final QuestionnaireService questionnaireService;
    private final QuestionService questionService;
    private final SubmissionService submissionService;

    public QuestionnaireController(QuestionnaireService questionnaireService,
                                   QuestionService questionService,
                                   SubmissionService submissionService) {
        this.questionnaireService = questionnaireService;
        this.questionService = questionService;
        this.submissionService = submissionService;
    }

    @GetMapping("/questionnaires")
    public String list(Model model) {
        model.addAttribute("questionnaires", questionnaireService.findAll());
        return "questionnaires/list";
    }

    @GetMapping("/questionnaires/new")
    public String showCreateForm(Model model) {
        model.addAttribute("questionnaire", new Questionnaire());
        return "questionnaires/new";
    }

    @PostMapping("/questionnaires")
    public String create(@Valid @ModelAttribute("questionnaire") Questionnaire questionnaire,
                         BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "questionnaires/new";
        }

        questionnaireService.save(questionnaire);
        return "redirect:/questionnaires";
    }

    @GetMapping("/questionnaires/{id}/edit-questions")
    public String editQuestions(@PathVariable Long id, Model model) {
        var questionnaire = questionnaireService.findById(id);
        model.addAttribute("questionnaire", questionnaire);
        model.addAttribute("allQuestions", questionService.findAll());
        return "questionnaires/edit-questions";
    }

    @PostMapping("/questionnaires/{id}/questions")
    public String updateQuestions(@PathVariable Long id,
                                  @RequestParam(value = "questionIds", required = false) List<Long> questionIds,
                                  Model model) {
        try {
            List<Long> ids = (questionIds == null) ? List.of() : questionIds;
            var selected = questionService.findAllById(ids);
            questionnaireService.updateQuestions(id, new ArrayList<>(selected));
            return "redirect:/questionnaires";
        } catch (RuntimeException ex) {
            var questionnaire = questionnaireService.findById(id);
            model.addAttribute("questionnaire", questionnaire);
            model.addAttribute("allQuestions", questionService.findAll());
            model.addAttribute("error", ex.getMessage());
            return "questionnaires/edit-questions";
        }
    }

    @PostMapping("/questionnaires/{id}/finalize")
    public String finalizeQuestionnaire(@PathVariable Long id, Model model) {
        try {
            questionnaireService.finalizeQuestionnaire(id);
            return "redirect:/questionnaires";
        } catch (RuntimeException ex) {
            model.addAttribute("questionnaires", questionnaireService.findAll());
            model.addAttribute("error", ex.getMessage());
            return "questionnaires/list";
        }
    }

    @GetMapping("/questionnaires/{id}/submissions")
    public String listSubmissions(@PathVariable Long id, Model model) {
        var questionnaire = questionnaireService.findById(id);
        model.addAttribute("questionnaire", questionnaire);
        model.addAttribute("submissions", submissionService.listByQuestionnaireId(id));
        return "questionnaires/submissions";
    }

    @PostMapping("/questionnaires/{id}/delete")
    public String delete(@PathVariable Long id) {
        questionnaireService.deleteById(id);
        return "redirect:/questionnaires";
    }
}
