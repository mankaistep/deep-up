package me.manaki.deepup.entity;

import lombok.*;

import javax.persistence.*;

@Table(name = "tbl_user")
@Entity
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class User {

    @Id
    @Column(name = "user_username", nullable = false, length = 100)
    private String username;

    @Lob
    @Column(name = "user_hash_password", nullable = false)
    private String hashPassword;

    @Column(name = "user_account_type", nullable = false, length = 15)
    private String accountType;

    @Column(name = "user_full_name", nullable = false, length = 50)
    private String fullName;

    @Column(name = "user_email", length = 50)
    private String email;

    @Column(name ="user_avatar", length = 100)
    private String avatar;

    @Column(name = "user_role_id", nullable = false)
    private Integer roleId;

    @Column(name = "user_nick", length = 16)
    private String nick;

    @Column(name = "user_wallpaper", length = 100)
    private String wallpaper;

    @Column(name = "user_facebook", length = 500)
    private String facebook;

    @Column(name = "user_github", length = 500)
    private String github;

}