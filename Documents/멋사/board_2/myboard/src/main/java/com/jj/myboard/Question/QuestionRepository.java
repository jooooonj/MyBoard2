package com.jj.myboard.Question;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    @Modifying
    @Query("DELETE FROM Question")
    void deleteAllSiteUsers();
}
