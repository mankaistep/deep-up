package me.manaki.deepup.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "tbl_topic")
@Entity
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class Topic {

    @Id
    @Column(name = "topic_id", nullable = false)
    private Integer id;

    @Column(name = "topic_name", nullable = false, length = 50)
    private String name;

    @Column(name = "topic_image", nullable = false, length = 50)
    private String image;

    @Column(name = "topic_quote", nullable = false, length = 50)
    private String quote;

    @Column(name = "topic_url", nullable = false, length = 15)
    private String url;

}