package me.manaki.deepup.repository;

import me.manaki.deepup.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TopicRepository extends JpaRepository<Topic, Integer> {

    Optional<Topic> findByUrl(String url);

}
