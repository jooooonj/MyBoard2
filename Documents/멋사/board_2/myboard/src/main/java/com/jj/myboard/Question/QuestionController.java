package com.jj.myboard.Question;

import com.jj.myboard.SiteUser.SiteUser;
import com.jj.myboard.SiteUser.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/question")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;
    private final UserService userService;

    @GetMapping("list")
    public String list(Model model, @RequestParam(defaultValue = "0") int page) {
        Page<Question> questionList = questionService.getQuestionList(page);

        int blockPage = 5;
        int startPage = page / blockPage * blockPage + 1;
        int endPage = startPage + 4 > questionList.getTotalPages() ? questionList.getTotalPages() : startPage + 4;
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

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String createForm(QuestionForm questionForm){
        return "question/form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String createForm(@Valid QuestionForm questionForm, BindingResult bindingResult, Principal principal){
        if (bindingResult.hasErrors()) {
            return "redirect:/question/create";
        }

        SiteUser user = userService.getUser(principal.getName());
        questionService.addQuestion(questionForm.getSubject(), questionForm.getContent(), user);

        return "redirect:/question/list";
    }
}
