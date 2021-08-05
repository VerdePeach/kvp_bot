package sumdu.kvp.bot.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sumdu.kvp.bot.dto.TopicDTORequest;
import sumdu.kvp.bot.dto.TopicDTOResponse;
import sumdu.kvp.bot.mapper.TopicMapper;
import sumdu.kvp.bot.model.Topic;
import sumdu.kvp.bot.repository.TopicRepository;
import sumdu.kvp.bot.service.TopicService;

import java.util.List;

@Service
public class TopicServiceImpl implements TopicService {

    private TopicRepository topicRepository;
    private TopicMapper topicMapper;

    @Autowired
    public TopicServiceImpl(TopicRepository topicRepository, TopicMapper topicMapper) {
        this.topicRepository = topicRepository;
        this.topicMapper = topicMapper;
    }

    @Override
    public TopicDTOResponse create(TopicDTORequest topicDTORequest) throws Exception {
        topicRepository.saveWithFather(
                topicDTORequest.getName(),
                topicDTORequest.getText(),
                topicDTORequest.getType(),
                topicDTORequest.getFatherId());

        var topic = topicRepository.findByName(topicDTORequest.getName());
        if (topic == null) {
            //TODO
            throw new Exception("Topic creating error");
        }
        return topicMapper.topicToTopicDTOResponse(topic);

    }

    @Override
    public List<TopicDTOResponse> findAll() {
        return topicMapper.topicsToTopicDTOsResponse(topicRepository.findAll());
    }

    @Override
    public List<Topic> findByFatherId(Integer fatherId) {
        return topicRepository.findByFatherId(fatherId);
    }

    @Override
    public List<Topic> findBasicTopics() {
        return topicRepository.findByFatherIdIsNull();
    }

    @Override
    public Topic findTopicByName(String topicName) {
        return topicRepository.findByName(topicName);

    }

    @Override
    public Topic findFatherByChild(Integer childId) {
        return topicRepository.findFatherByChild(childId);
    }

    @Override
    public void deleteById(Integer id) {
        topicRepository.deleteById(id);
    }

    @Override
    public TopicDTOResponse update(TopicDTORequest topicDTORequest, Integer id) throws Exception {
        topicRepository.updateWithFather(topicDTORequest.getName(), topicDTORequest.getText(),
                topicDTORequest.getType(),topicDTORequest.getFatherId(), id);

        var topic = topicRepository.findByName(topicDTORequest.getName());
        if (topic == null) {
            //TODO
            throw new Exception("Topic creating error");
        }
        return topicMapper.topicToTopicDTOResponse(topic);
    }

}
