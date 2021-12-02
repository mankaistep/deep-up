package me.manaki.deepup.entity;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;

@Table(name = "tbl_comment")
@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id", nullable = false)
    private Integer id;

    @Lob
    @Column(name = "comment_content", nullable = false)
    private String content;

    @Column(name = "comment_creation_date", nullable = false)
    private Instant creationDate;

    @Column(name = "comment_username", nullable = false, length = 100)
    private String username;

    @Column(name = "comment_parent_id")
    private Integer parentId;

    @Column(name = "comment_post_id", nullable = false)
    private Integer postId;

    public Comment(String content, Instant creationDate, String username, Integer parentId, Integer postId) {
        this.content = content;
        this.creationDate = creationDate;
        this.username = username;
        this.parentId = parentId;
        this.postId = postId;
    }
}