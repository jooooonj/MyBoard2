package com.jj.myboard.Question;

import com.jj.myboard.Answer.Answer;
import com.jj.myboard.Answer.AnswerForm;
import com.jj.myboard.SiteUser.SiteUser;
import com.jj.myboard.SiteUser.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    public String detail(@PathVariable(value = "id") long id, Model model, AnswerForm answerForm, @RequestParam int page) {

        Question question = questionService.getQuestion(id);
        model.addAttribute("question", question);
        model.addAttribute("page", page);

        return "question/detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String createForm(QuestionForm questionForm){
        return "question/form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String createQuestion(@Valid QuestionForm questionForm, BindingResult bindingResult, Principal principal){
        if (bindingResult.hasErrors()) {
            return "redirect:/question/create";
        }

        SiteUser user = userService.getUser(principal.getName());
        questionService.addQuestion(questionForm.getSubject(), questionForm.getContent(), user);

        return "redirect:/question/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String modify(@PathVariable(value = "id") long id, Principal principal, QuestionForm questionForm,
                         Model model, @RequestParam int page) {
        Question question = questionService.getQuestion(id);

        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        model.addAttribute("page", page);

        questionForm.setSubject(question.getSubject());
        questionForm.setContent(question.getContent());
        return "question/form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String modify(@Valid QuestionForm questionForm, BindingResult bindingResult,
                         @PathVariable(value = "id") long id, Principal principal, @RequestParam int page) {
        if (bindingResult.hasErrors()) {
            return "question/form";
        }

        Question question = questionService.getQuestion(id);

        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        questionService.modify(question, questionForm.getSubject(), questionForm.getContent());
        return String.format("redirect:/question/detail/%s?page=%s", id, page);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable(value = "id") long id, Principal principal, HttpServletRequest request) {
        String referrer = request.getHeader("referer"); //이전 코드
        String[] split = referrer.substring(referrer.indexOf('?')).split("\\=");
        String page = split[1];

        Question question = questionService.getQuestion(id);

        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        questionService.delete(question);
        return String.format("redirect:/question/list?page=%s", page);
    }

    @GetMapping("/vote/{id}")
    public String vote(@PathVariable(value = "id") long id, Principal principal, HttpServletRequest request) {
        String referer = request.getHeader("referer");
        Question question = questionService.getQuestion(id);
        SiteUser user = userService.getUser(principal.getName());

        questionService.addVoter(question, user);
        return String.format("redirect:%s", referer);

    }

}
