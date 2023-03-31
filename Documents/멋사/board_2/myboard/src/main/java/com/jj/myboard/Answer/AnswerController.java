package com.jj.myboard.Answer;

import com.jj.myboard.Question.Question;
import com.jj.myboard.Question.QuestionForm;
import com.jj.myboard.Question.QuestionService;
import com.jj.myboard.SiteUser.SiteUser;
import com.jj.myboard.SiteUser.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/answer")
public class AnswerController {
    private final AnswerService answerService;
    private final QuestionService questionService;
    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/add/{id}")
    public String addAnswer(@Valid AnswerForm answerForm, BindingResult bindingResult,
                            @PathVariable(value = "id") long id, Principal principal) {

        if (bindingResult.hasErrors())
            return String.format("redirect:/question/detail/%s", id);

        Question question = questionService.getQuestion(id);
        SiteUser user = userService.getUser(principal.getName());

        answerService.addAnswer(question, answerForm.getContent(), user);
        return String.format("redirect:/question/detail/%s", id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String modify(@PathVariable(value = "id") long id, Principal principal, AnswerForm answerForm) {
        Answer answer = answerService.getAnswer(id);

        if (!answer.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        answerForm.setContent(answer.getContent());
        return "answer/form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String modify(@Valid AnswerForm answerForm, BindingResult bindingResult,
                         @PathVariable(value = "id") long id, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "answer/form";
        }

        Answer answer = answerService.getAnswer(id);

        if (!answer.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        answerService.modify(answer, answerForm.getContent());
        return "redirect:/question/detail/" + answer.getQuestion().getId();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable(value = "id") long id, Principal principal) {
        Answer answer = answerService.getAnswer(id);

        if (!answer.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        answerService.delete(answer);
        return "redirect:/question/detail/" + answer.getQuestion().getId();
    }
}
