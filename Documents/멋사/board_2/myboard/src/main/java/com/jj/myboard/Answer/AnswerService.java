package com.jj.myboard.Answer;

import com.jj.myboard.DataNotFoundException;
import com.jj.myboard.Question.Question;
import com.jj.myboard.SiteUser.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;

    public Answer addAnswer(Question question, String content, SiteUser user) {
        Answer answer = Answer
                .builder()
                .author(user)
                .content(content)
                .question(question)
                .build();

        question.addAnswer(answer);
        user.addAnswer(answer);
        return answerRepository.save(answer);
    }

    public Answer getAnswer(long id) {
        Optional<Answer> _answer = answerRepository.findById(id);
        if(_answer.isEmpty())
            throw new DataNotFoundException("답변을 등록해주세요");

        return _answer.get();
    }

    public void modify(Answer answer, String content) {
        answer.modify(content);
        answerRepository.save(answer);
    }

    public void delete(Answer answer) {
        answerRepository.delete(answer);
    }

    public void addVoter(Answer answer, SiteUser user) {
        answer.addVoter(user);
        answerRepository.save(answer);
    }
}
