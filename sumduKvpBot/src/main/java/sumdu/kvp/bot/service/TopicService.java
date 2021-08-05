package sumdu.kvp.bot.service;

import sumdu.kvp.bot.dto.TopicDTOResponse;
import sumdu.kvp.bot.model.Topic;
import sumdu.kvp.bot.dto.TopicDTORequest;

import java.util.List;

public interface TopicService {
   TopicDTOResponse create(TopicDTORequest topicDTORequest) throws Exception;
   TopicDTOResponse update(TopicDTORequest topicDTORequest, Integer id) throws Exception;
   List<TopicDTOResponse> findAll();
   List<Topic> findByFatherId(Integer fatherId);
   List<Topic> findBasicTopics();
   Topic findTopicByName(String name);
   Topic findFatherByChild(Integer childId);
   void deleteById(Integer id);
}
