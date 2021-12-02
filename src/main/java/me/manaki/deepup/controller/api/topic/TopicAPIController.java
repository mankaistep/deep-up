package me.manaki.deepup.controller.api.topic;

import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import me.manaki.deepup.entity.Topic;
import me.manaki.deepup.service.TopicService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@RequestMapping("/api/topic")
@RestController
@RequiredArgsConstructor
public class TopicAPIController {

    private final TopicService topicService;

    @GetMapping("/get")
    public Topic getTopic(@RequestParam Integer topicId) throws NotFoundException {
        return topicService.getTopic(topicId).orElseThrow(() -> new NotFoundException("Can't find topic with id " + topicId));
    }

    @GetMapping("/get-all")
    public List<Topic> getAllTopics() {
        return topicService.getAllTopic();
    }

}
