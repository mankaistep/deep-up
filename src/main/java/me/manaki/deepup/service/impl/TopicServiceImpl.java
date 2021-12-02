package me.manaki.deepup.service.impl;

import lombok.RequiredArgsConstructor;
import me.manaki.deepup.entity.Topic;
import me.manaki.deepup.repository.TopicRepository;
import me.manaki.deepup.service.TopicService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TopicServiceImpl implements TopicService {

    private final TopicRepository topicRepository;

    @Override
    public Optional<Topic> getTopic(Integer id) {
        return topicRepository.findById(id);
    }

    @Override
    public Optional<Topic> getByUrl(String url) {
        return topicRepository.findByUrl(url);
    }

    @Override
    public List<Topic> getAllTopic() {
        return topicRepository.findAll();
    }

}
