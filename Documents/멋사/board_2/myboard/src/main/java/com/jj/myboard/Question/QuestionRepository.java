package com.jj.myboard.Question;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    @Modifying
    @Query("DELETE FROM Question")
    void deleteAllSiteUsers();

    Page<Question> findAll(Pageable pageable);

    Page<Question> findAll(Specification<Question> spec, Pageable pageable);
}
