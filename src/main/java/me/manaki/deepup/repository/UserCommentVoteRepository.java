package me.manaki.deepup.repository;

import me.manaki.deepup.entity.UserCommentVote;
import me.manaki.deepup.entity.id.UserCommentVoteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserCommentVoteRepository extends JpaRepository<UserCommentVote, UserCommentVoteId> {

    @Query("select count(ucv) from UserCommentVote ucv where ucv.id.commentId=:commentId and ucv.isUpvote=true")
    Integer countUpvotes(Integer commentId);

    @Query("select count(ucv) from UserCommentVote ucv where ucv.id.commentId=:commentId and ucv.isUpvote=false")
    Integer countDownvotes(Integer commentId);

    @Query("select ucv from UserCommentVote ucv where ucv.id.username = :username and ucv.isUpvote=true and ucv.id.commentId = :commentId")
    Optional<UserCommentVote> isUpvoted(String username, Integer commentId);

    @Query("select ucv from UserCommentVote ucv where ucv.id.username = :username and ucv.isUpvote=false and ucv.id.commentId = :commentId")
    Optional<UserCommentVote> isDownvoted(String username, Integer commentId);

}
