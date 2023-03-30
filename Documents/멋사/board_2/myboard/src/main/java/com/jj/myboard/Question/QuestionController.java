package com.jj.myboard.Question;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/question")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

    @GetMapping("list")
    public String list(Model model, @RequestParam(defaultValue = "0") int page) {
        Page<Question> questionList = questionService.getQuestionList(page);

        int blockPage = 5;
        int startPage = page / blockPage * blockPage + 1;
        int endPage = startPage + 4;
        model.addAttribute("questionList", questionList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "question/list";
    }

    @GetMapping("detail/{id}")
    public String detail(@PathVariable(value = "id") long id, Model model) {
        Question question = questionService.getQuestion(id);
        model.addAttribute("question", question);

        return "question/detail";
    }
}
