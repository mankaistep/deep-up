package me.manaki.deepup.entity.id;

import lombok.AllArgsConstructor;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@AllArgsConstructor
public class UserVoteId implements Serializable {

    private static final long serialVersionUID = 7986331554256181293L;
    @Column(name = "username", nullable = false, length = 100)
    private String username;
    @Column(name = "post_id", nullable = false)
    private Integer postId;

    public UserVoteId() {

    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, username);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserVoteId entity = (UserVoteId) o;
        return Objects.equals(this.postId, entity.postId) &&
                Objects.equals(this.username, entity.username);
    }
}