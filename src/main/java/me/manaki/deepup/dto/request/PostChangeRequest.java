package me.manaki.deepup.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class PostChangeRequest {

    private Integer id;

    @Min(value = 0, message = "Chọn chủ đề đi ông nội")
    private Integer topicId;

    @NotBlank(message = "Không có tiêu đề, thật luôn?")
    private String title;

    @NotBlank(message = "Điền cái mô tả cái, cảm ơn")
    private String subtitle;

    @NotBlank(message = "Đăng bài nhưng content rỗng thì nhất ông rồi")
    private String content;

    private MultipartFile image;

}
