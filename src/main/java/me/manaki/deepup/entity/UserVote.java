package me.manaki.deepup.entity;

import lombok.AllArgsConstructor;
import me.manaki.deepup.entity.id.UserVoteId;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Table(name = "tbl_user_vote")
@Entity
@AllArgsConstructor
public class UserVote {

    @EmbeddedId
    private UserVoteId id;

    @Column(name = "is_upvote", nullable = false)
    private Boolean isUpvote = false;

    public UserVote() {

    }

    public Boolean getIsUpvote() {
        return isUpvote;
    }

    public void setIsUpvote(Boolean isUpvote) {
        this.isUpvote = isUpvote;
    }

    public UserVoteId getId() {
        return id;
    }

    public void setId(UserVoteId id) {
        this.id = id;
    }

}