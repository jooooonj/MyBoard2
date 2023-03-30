package com.jj.myboard.Question;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionForm {

    @NotBlank(message = "제목은 필수입니다.")
    private String subject;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

}
