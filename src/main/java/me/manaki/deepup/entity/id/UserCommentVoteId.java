package me.manaki.deepup.entity.id;

import lombok.AllArgsConstructor;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@AllArgsConstructor
public class UserCommentVoteId implements Serializable {
    private static final long serialVersionUID = -2849162366377713803L;
    @Column(name = "username", nullable = false, length = 100)
    private String username;
    @Column(name = "comment_id", nullable = false)
    private Integer commentId;

    public UserCommentVoteId() {

    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public int hashCode() {
        return Objects.hash(commentId, username);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserCommentVoteId entity = (UserCommentVoteId) o;
        return Objects.equals(this.commentId, entity.commentId) &&
                Objects.equals(this.username, entity.username);
    }
}