package com.jj.myboard.Question;

import com.jj.myboard.Answer.Answer;
import com.jj.myboard.DataNotFoundException;
import com.jj.myboard.SiteUser.SiteUser;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;

    private Specification<Question> search(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Question> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);  // 중복을 제거
                Join<Question, SiteUser> u1 = q.join("author", JoinType.LEFT);
                Join<Question, Answer> a = q.join("answerList", JoinType.LEFT);
                Join<Answer, SiteUser> u2 = a.join("author", JoinType.LEFT);
                return cb.or(cb.like(q.get("subject"), "%" + kw + "%"), // 제목
                        cb.like(q.get("content"), "%" + kw + "%"),      // 내용
                        cb.like(u1.get("name"), "%" + kw + "%"),    // 질문 작성자
                        cb.like(a.get("content"), "%" + kw + "%"),      // 답변 내용
                        cb.like(u2.get("name"), "%" + kw + "%"));   // 답변 작성자
            }
        };
    }
    public Page<Question> getQuestionList(int page, String kw) {
        int pageLimit = 10;
        Pageable pageable = PageRequest.of(page, pageLimit, Sort.Direction.DESC, "createDate");

        Specification<Question> spec = search(kw);
        Page<Question> questionList = questionRepository.findAll(spec, pageable);
        return questionList;
    }

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
        Pageable pageable = PageRequest.of(page, pageLimit, Sort.Direction.DESC, "createDate");

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

    public void addVoter(Question question, SiteUser user) {
        question.addVoter(user);
        questionRepository.save(question);
    }
}
