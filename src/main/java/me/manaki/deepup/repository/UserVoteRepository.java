package me.manaki.deepup.repository;

import me.manaki.deepup.entity.UserVote;
import me.manaki.deepup.entity.id.UserVoteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserVoteRepository extends JpaRepository<UserVote, UserVoteId> {

    @Query("select count(uv) from UserVote uv where uv.id.postId=:postId and uv.isUpvote=true")
    Integer countUpvotes(Integer postId);

    @Query("select count(uv) from UserVote uv where uv.id.postId=:postId and uv.isUpvote=false")
    Integer countDownvotes(Integer postId);

}
