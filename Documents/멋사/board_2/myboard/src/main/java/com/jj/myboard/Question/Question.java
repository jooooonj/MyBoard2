package com.jj.myboard.Question;

import com.jj.myboard.Answer.Answer;
import com.jj.myboard.SiteUser.SiteUser;
import com.jj.myboard.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Question extends BaseEntity {

    private String subject;
    private String content;
    @ManyToOne
    private SiteUser author;

    @Builder.Default
    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private List<Answer> answerList = new ArrayList<>();

    @ManyToMany
    @Builder.Default
    private Set<SiteUser> voter = new LinkedHashSet<>();
}
