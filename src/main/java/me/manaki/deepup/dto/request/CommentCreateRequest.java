package me.manaki.deepup.dto.request;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@RequiredArgsConstructor
public class CommentCreateRequest {

    private Integer parentId;

    @NotBlank(message = "Nhập bình luận đi bạn?")
    private String content;

}
