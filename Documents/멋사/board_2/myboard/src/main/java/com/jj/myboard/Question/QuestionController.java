package com.jj.myboard.Question;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/question")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

    @GetMapping("list")
    public String list(Model model) {
        List<Question> questionList = questionService.getQuestionList();

        model.addAttribute("questionList", questionList);
        return "question/list";
    }

    @GetMapping("detail/{id}")
    public String detail(@PathVariable(value = "id") long id, Model model) {
        Question question = questionService.getQuestion(id);
        model.addAttribute("question", question);

        return "question/detail";
    }
}
