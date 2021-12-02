package me.manaki.deepup.repository;

import me.manaki.deepup.entity.Post;
import me.manaki.deepup.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {

    @Modifying
    @Transactional
    @Query("update Post p set p.views = :views where p.id = :postId")
    void updateViews(Integer postId, Integer views);

    @Query(nativeQuery = true, value = "select top 10 post.post_id from tbl_post as post where post.post_hidden = 0 order by post.post_views desc")
    List<Integer> getTop10Posts();

    @Query(nativeQuery = true, value = "select top 3 post.post_id from tbl_post as post where post.post_topic_id = :topicId and post.post_hidden = 0 order by post.post_views desc")
    List<Integer> getTop3Posts(Integer topicId);

    @Query(nativeQuery = true, value = "select post_id\n" +
            "from tbl_post as post where post.post_topic_id = :topicId and post.post_hidden = 0\n" +
            "order by post.post_creation_date desc\n" +
            "offset 5 * (:page - 1) rows\n" +
            "fetch next 5 rows only")
    List<Integer> getPagedPosts(Integer topicId, Integer page);

}
