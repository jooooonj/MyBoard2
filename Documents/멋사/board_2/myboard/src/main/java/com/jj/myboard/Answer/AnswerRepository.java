package com.jj.myboard.Answer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    @Modifying
    @Query("DELETE FROM Answer")
    void deleteAllSiteUsers();
}
