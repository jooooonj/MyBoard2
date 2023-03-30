package com.jj.myboard.Answer;

import com.jj.myboard.Question.Question;
import com.jj.myboard.SiteUser.SiteUser;
import com.jj.myboard.shared.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Answer extends BaseEntity {

    private String content;
    @ManyToOne
    private Question question;

    @ManyToOne
    private SiteUser author;
    @ManyToMany
    @Builder.Default
    private Set<SiteUser> voter = new LinkedHashSet<>();
}
