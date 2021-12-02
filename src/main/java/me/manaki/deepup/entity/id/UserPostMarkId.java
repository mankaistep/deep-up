package me.manaki.deepup.entity.id;

import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserPostMarkId implements Serializable {

    private static final long serialVersionUID = -6631358391060007069L;

    @Column(name = "username", nullable = false, length = 100)
    private String username;
    @Column(name = "post_id", nullable = false)
    private Integer postId;

    public UserPostMarkId() {
    }

    public UserPostMarkId(String username, Integer postId) {
        this.username = username;
        this.postId = postId;
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
        UserPostMarkId entity = (UserPostMarkId) o;
        return Objects.equals(this.postId, entity.postId) &&
                Objects.equals(this.username, entity.username);
    }
}