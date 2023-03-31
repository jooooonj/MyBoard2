package com.jj.myboard.SiteUser;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterForm {

    @NotEmpty(message = "아이디는 필수값입니다.")
    @Size(min = 3, max = 25)
    private String username;

    @NotEmpty(message = "비밀번호는 필수값입니다.")
    private String password;
    @NotEmpty(message = "비밀번호 확인은 필수입니다.")
    private String password2;

    @NotEmpty(message = "이름은 필수값입니다.")
    private String name;

    @Email(message = "이메일 형식으로 입력해주세요.")
    @NotEmpty(message = "이메일은 필수값입니다.")
    private String email;
}
