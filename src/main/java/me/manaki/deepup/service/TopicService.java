package me.manaki.deepup.service;

import me.manaki.deepup.entity.Topic;

import java.util.List;
import java.util.Optional;

public interface TopicService {

    Optional<Topic> getTopic(Integer id);
    Optional<Topic> getByUrl(String url);
    List<Topic> getAllTopic();

}
