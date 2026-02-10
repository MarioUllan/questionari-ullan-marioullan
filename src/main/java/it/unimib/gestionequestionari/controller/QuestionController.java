package it.unimib.gestionequestionari.controller;

import it.unimib.gestionequestionari.model.Question;
import it.unimib.gestionequestionari.model.QuestionType;
import it.unimib.gestionequestionari.service.QuestionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class QuestionController {

    private final QuestionService service;

    public QuestionController(QuestionService service) {
        this.service = service;
    }

    @GetMapping("/questions")
    public String list(Model model) {
        model.addAttribute("questions", service.findAll());
        return "questions/list";
    }

    @GetMapping("/questions/new")
    public String showCreateForm(Model model) {
        model.addAttribute("question", new Question());
        model.addAttribute("types", QuestionType.values());
        return "questions/new";
    }

    @PostMapping("/questions")
    public String create(@ModelAttribute Question question) {
        service.save(question);
        return "redirect:/questions";
    }
}
