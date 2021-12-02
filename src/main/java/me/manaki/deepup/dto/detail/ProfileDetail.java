package me.manaki.deepup.dto.detail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileDetail {

    private String username;

    private String nick;

    private String email;

    private String fullName;

    private String avatar;

    private String wallpaper;

    private String facebook;

    private String github;

    private Integer votes;

    private Integer views;

    private Integer posts;

    private Integer followers;

    private Integer followings;

}
