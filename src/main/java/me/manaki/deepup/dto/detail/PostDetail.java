package me.manaki.deepup.dto.detail;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostDetail {

    private Integer id;

    private String title;

    private String subtitle;

    private String content;

    private String image;

    private Integer views;

    private Integer votes;

    private String creationDate;

    private String username;

    private String userFullname;

    private String avatar;

    private String topic;

    private Boolean marked;

    private String nick;

    private String editDate;

    private Boolean hidden;

}
