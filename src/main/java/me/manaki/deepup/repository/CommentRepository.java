package me.manaki.deepup.repository;

import me.manaki.deepup.entity.Comment;
import me.manaki.deepup.entity.UserCommentVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    @Query("select c from Comment c where c.postId = :postId and c.parentId is null order by c.creationDate desc")
    List<Comment> getGreatComments(Integer postId);

    List<Comment> findByParentId(Integer parentId);

}
