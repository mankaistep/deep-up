package me.manaki.deepup.dto.request;

import lombok.Data;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Objects;

@Data
public class PasswordChangeRequest {

    @NotBlank(message="?")
    private String oldPassword;

    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[a-zA-Z]).{8,}$", message="Độ dài hơn 8, ít nhất 1 chữ số")
    private String password;

    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[a-zA-Z]).{8,}$", message="? Nhập cc gì v")
    private String repeatPassword;

    @AssertTrue(message = "Mật khẩu nhập lại không giống!")
    public boolean isValidPassword() {
        return Objects.nonNull(password) && password.equals(repeatPassword);
    }

}
