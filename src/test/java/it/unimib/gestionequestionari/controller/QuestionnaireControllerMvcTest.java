package it.unimib.gestionequestionari.controller;

import it.unimib.gestionequestionari.service.QuestionService;
import it.unimib.gestionequestionari.service.QuestionnaireService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(QuestionnaireController.class)
class QuestionnaireControllerMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QuestionnaireService questionnaireService;

    @MockBean
    private QuestionService questionService;

    @Test
    void list_shouldReturnViewAndModel() throws Exception {
        when(questionnaireService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/questionnaires"))
                .andExpect(status().isOk())
                .andExpect(view().name("questionnaires/list"))
                .andExpect(model().attributeExists("questionnaires"));
    }
}