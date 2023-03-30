package com.jj.myboard.Question;

import com.jj.myboard.DataNotFoundException;
import com.jj.myboard.SiteUser.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;

    public Question addQuestion(String subject, String content, SiteUser user) {
        Question question = Question
                .builder()
                .subject(subject)
                .content(content)
                .author(user)
                .build();

        user.addQuestion(question);
        return questionRepository.save(question);
    }

    public void deleteAll(){
        questionRepository.deleteAll();
    }

    public List<Question> getQuestionList() {
        return questionRepository.findAll();
    }

    public Question getQuestion(long id) {
        Optional<Question> _question = questionRepository.findById(id);
        if (_question.isEmpty()) {
            throw new DataNotFoundException("question is not found");
        }
        return _question.get();
    }
}
