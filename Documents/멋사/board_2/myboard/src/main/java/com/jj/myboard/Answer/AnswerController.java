package com.jj.myboard.Answer;

import com.jj.myboard.Question.Question;
import com.jj.myboard.Question.QuestionForm;
import com.jj.myboard.Question.QuestionService;
import com.jj.myboard.SiteUser.SiteUser;
import com.jj.myboard.SiteUser.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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
                            @PathVariable(value = "id") long id, Principal principal, HttpServletRequest request) {

        String referrer = request.getHeader("referer"); //이전 코드

        if (bindingResult.hasErrors())
            return String.format("redirect:%s", referrer);


        Question question = questionService.getQuestion(id);
        SiteUser user = userService.getUser(principal.getName());

        Answer answer = answerService.addAnswer(question, answerForm.getContent(), user);
        return String.format("redirect:%s#answer_%s", referrer, answer.getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String modify(@PathVariable(value = "id") long id, Principal principal, AnswerForm answerForm, @RequestParam int page
    , Model model) {
        Answer answer = answerService.getAnswer(id);

        if (!answer.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        model.addAttribute("page", page);
        answerForm.setContent(answer.getContent());
        return "answer/form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String modify(@Valid AnswerForm answerForm, BindingResult bindingResult,
                         @PathVariable(value = "id") long id, Principal principal, @RequestParam int page) {
        if (bindingResult.hasErrors()) {
            return "answer/form";
        }

        Answer answer = answerService.getAnswer(id);

        if (!answer.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        answerService.modify(answer, answerForm.getContent());
        return String.format("redirect:/question/detail/%s?page=%s#answer_%s", answer.getQuestion().getId(), page, answer.getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable(value = "id") long id, Principal principal, HttpServletRequest request) {
        String referrer = request.getHeader("referer"); //이전 코드
        Answer answer = answerService.getAnswer(id);

        if (!answer.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        answerService.delete(answer);
        return String.format("redirect:%s", referrer);
    }

    @GetMapping("/vote/{id}")
    public String vote(@PathVariable(value = "id") long id, Principal principal, HttpServletRequest request) {
        String referrer = request.getHeader("referer"); //이전 코드

        Answer answer = answerService.getAnswer(id);
        SiteUser user = userService.getUser(principal.getName());

        answerService.addVoter(answer, user);
        return String.format("redirect:%s#answer_%s", referrer, answer.getId());
    }
}
