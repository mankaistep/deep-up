package me.manaki.deepup.entity;

import javax.persistence.*;
import java.time.Instant;

@Table(name = "tbl_post")
@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id", nullable = false)
    private Integer id;

    @Column(name = "post_title", nullable = false, length = 100)
    private String title;

    @Column(name = "post_subtitle", nullable = false, length = 500)
    private String subtitle;

    @Lob
    @Column(name = "post_content", nullable = false)
    private String content;

    @Column(name = "post_image", nullable = false, length = 50)
    private String image;

    @Column(name = "post_views", nullable = false)
    private Integer views;

    @Column(name = "post_topic_id", nullable = false)
    private Integer topicId;

    @Column(name = "post_username", nullable = false, length = 100)
    private String username;

    @Column(name = "post_creation_date", nullable = false)
    private Instant creationDate;

    @Column(name = "post_edit_date")
    private Instant editDate;

    @Column(name = "post_hidden")
    private Boolean hidden;

    public Boolean getHidden() {
        return hidden;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }

    public Instant getEditDate() {
        return editDate;
    }

    public void setEditDate(Instant editDate) {
        this.editDate = editDate;
    }

    public Post(String title, String subtitle, String content, String image, Integer views, Integer topicId, String username, Instant creationDate, Instant editDate, Boolean hidden) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.content = content;
        this.image = image;
        this.views = views;
        this.topicId = topicId;
        this.username = username;
        this.creationDate = creationDate;
        this.editDate = editDate;
        this.hidden = hidden;
    }

    public Post() {

    }

    public Integer getTopicId() {
        return topicId;
    }

    public void setTopicId(Integer topicId) {
        this.topicId = topicId;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

}