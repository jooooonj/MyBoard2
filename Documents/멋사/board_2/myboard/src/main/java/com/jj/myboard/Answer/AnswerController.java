package com.jj.myboard.Answer;

import com.jj.myboard.Question.Question;
import com.jj.myboard.Question.QuestionService;
import com.jj.myboard.SiteUser.SiteUser;
import com.jj.myboard.SiteUser.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class AnswerController {
    private final AnswerService answerService;
    private final QuestionService questionService;
    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/answer/add/{id}")
    public String addAnswer(@Valid AnswerForm answerForm, BindingResult bindingResult,
                            @PathVariable(value = "id") long id, Principal principal) {

        if(bindingResult.hasErrors())
            return String.format("redirect:/question/detail/%s", id);

        Question question = questionService.getQuestion(id);
        SiteUser user = userService.getUser(principal.getName());

        answerService.addAnswer(question, answerForm.getContent(), user);
        return String.format("redirect:/question/detail/%s", id);
    }
}
