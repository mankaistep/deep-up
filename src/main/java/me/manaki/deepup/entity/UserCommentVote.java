package me.manaki.deepup.entity;

import me.manaki.deepup.entity.id.UserCommentVoteId;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Table(name = "tbl_user_comment_vote")
@Entity
public class UserCommentVote {

    @EmbeddedId
    private UserCommentVoteId id;

    @Column(name = "is_upvote", nullable = false)
    private Boolean isUpvote = false;

    public UserCommentVote(UserCommentVoteId id, Boolean isUpvote) {
        this.id = id;
        this.isUpvote = isUpvote;
    }

    public UserCommentVote() {

    }

    public Boolean getIsUpvote() {
        return isUpvote;
    }

    public void setIsUpvote(Boolean isUpvote) {
        this.isUpvote = isUpvote;
    }

    public UserCommentVoteId getId() {
        return id;
    }

    public void setId(UserCommentVoteId id) {
        this.id = id;
    }
}