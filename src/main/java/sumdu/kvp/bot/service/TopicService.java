package sumdu.kvp.bot.service;

import sumdu.kvp.bot.dto.TopicDTOResponse;
import sumdu.kvp.bot.model.Topic;
import sumdu.kvp.bot.dto.TopicDTORequest;

import java.util.List;

public interface TopicService {
   TopicDTOResponse create(TopicDTORequest topicDTORequest);
   List<TopicDTOResponse> findAll();
   List<TopicDTOResponse> findByFatherId(Integer fatherId);
   List<TopicDTOResponse> findBasicTopics();
   TopicDTOResponse findTopicByName(String name);
   TopicDTOResponse findFatherByChild(Integer childId);
}
