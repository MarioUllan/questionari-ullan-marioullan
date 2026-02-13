package it.unimib.gestionequestionari.controller;

import it.unimib.gestionequestionari.model.Question;
import it.unimib.gestionequestionari.model.QuestionType;
import it.unimib.gestionequestionari.service.QuestionService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    public String create(@Valid @ModelAttribute("question") Question question,
                         BindingResult bindingResult,
                         Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("types", QuestionType.values());
            return "questions/new";
        }

        service.save(question);
        return "redirect:/questions";
    }
    @PostMapping("/questions/{id}/delete")
    public String delete(@PathVariable Long id) {
        service.deleteById(id);
        return "redirect:/questions";
    }

}
