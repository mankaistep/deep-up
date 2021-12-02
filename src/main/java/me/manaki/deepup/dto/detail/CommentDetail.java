package me.manaki.deepup.dto.detail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDetail {

    private Integer id;

    private String fullName;

    private String avatar;

    private String creationDate;

    private String content;

    private Integer votes;

    private List<CommentDetail> childs;

    private Boolean upVoted;

    private Boolean downVoted;

}
