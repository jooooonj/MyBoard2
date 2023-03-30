package com.jj.myboard.Answer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;

}
