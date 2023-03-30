package com.jj.myboard.SiteUser;

import com.jj.myboard.Answer.Answer;
import com.jj.myboard.Question.Question;
import com.jj.myboard.shared.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SiteUser extends BaseEntity {

    @Column(unique = true)
    private String username;
    private String password;
    private String name;
    private String email;
    @OneToMany(mappedBy = "author")
    @Builder.Default
    private List<Question> questionList = new ArrayList<>();

    @OneToMany(mappedBy = "author")
    @Builder.Default
    private List<Answer> answerList = new ArrayList<>();


    public void addQuestion(Question question) {
        questionList.add(question);
    }

    public void addAnswer(Answer answer) {
        answerList.add(answer);
    }
}
