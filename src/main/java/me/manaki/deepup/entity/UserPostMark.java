package me.manaki.deepup.entity;

import me.manaki.deepup.entity.id.UserPostMarkId;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.Instant;

@Table(name = "tbl_user_post_mark")
@Entity
public class UserPostMark {

    @EmbeddedId
    private UserPostMarkId id;

    @Column(name = "mark_date", nullable = false)
    private Instant markDate;

    public UserPostMark(UserPostMarkId id, Instant markDate) {
        this.id = id;
        this.markDate = markDate;
    }

    public UserPostMark() {

    }

    public Instant getMarkDate() {
        return markDate;
    }

    public void setMarkDate(Instant markDate) {
        this.markDate = markDate;
    }

    public UserPostMarkId getId() {
        return id;
    }

    public void setId(UserPostMarkId id) {
        this.id = id;
    }
}