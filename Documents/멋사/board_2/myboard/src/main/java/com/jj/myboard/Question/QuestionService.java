package com.jj.myboard.Question;

import com.jj.myboard.DataNotFoundException;
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

    public Page<Question> getQuestionList(int page) {
        int pageLimit = 10;
        Pageable pageable = PageRequest.of(page, pageLimit, Sort.Direction.DESC, "id");

        Page<Question> questionList = questionRepository.findAll(pageable);
        return questionList;
    }

    public Question getQuestion(long id) {
        Optional<Question> _question = questionRepository.findById(id);
        if (_question.isEmpty()) {
            throw new DataNotFoundException("question is not found");
        }
        return _question.get();
    }

    public void modify(Question question, String subject, String content) {
        question.modify(subject, content);
        questionRepository.save(question);
    }

    public void delete(Question question) {
        questionRepository.delete(question);
    }
}
