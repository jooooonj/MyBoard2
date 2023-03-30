package com.jj.myboard;

import com.jj.myboard.Question.QuestionRepository;
import com.jj.myboard.Question.QuestionService;
import com.jj.myboard.SiteUser.SiteUser;
import com.jj.myboard.SiteUser.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class MyboardApplicationTests {

	@Autowired
	private QuestionService questionService;
	@Autowired
	private UserService userService;

	@Test
	@DisplayName("유저 추가")
	void test01() {
		SiteUser user1 = userService.addUser("user1", "1234", "홍길동", "email@naver.com");
		SiteUser user2 = userService.addUser("user2", "1234", "홍길동2", "email@naver.com");
		SiteUser user3 = userService.addUser("user3", "1234", "홍길동3", "email@naver.com");
	}
	@Test
	@DisplayName("질문 추가")
	@Transactional
	@Rollback(value = false)
	void test02() {
		questionService.deleteAll();
		questionService.addQuestion("질문1", "내용1", userService.getUser(1));
		questionService.addQuestion("질문2", "내용2", userService.getUser(2));
		questionService.addQuestion("질문3", "내용3", userService.getUser(3));
	}

}
