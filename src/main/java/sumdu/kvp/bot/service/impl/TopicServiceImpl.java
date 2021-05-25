package sumdu.kvp.bot.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sumdu.kvp.bot.dto.TopicDTORequest;
import sumdu.kvp.bot.dto.TopicDTOResponse;
import sumdu.kvp.bot.mapper.TopicMapper;
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
    public TopicDTOResponse create(TopicDTORequest topicDTORequest) {
        topicRepository.saveWithFather(
                topicDTORequest.getName(),
                topicDTORequest.getText(),
                topicDTORequest.getFatherId());

        var topic = topicRepository.findByName(topicDTORequest.getName());
        if (topic.isEmpty()) {
            //TODO
            return null;
        } else {
            return topicMapper.topicToTopicDTOResponse(topic.get());
        }
    }

    @Override
    public List<TopicDTOResponse> findAll() {
        return topicMapper.topicsToTopicDTOsResponse(topicRepository.findAll());
    }

    @Override
    public List<TopicDTOResponse> findByFatherId(Integer fatherId) {
        var topic = topicRepository.findByFatherId(fatherId);
        if (topic.isEmpty()) {
            //TODO
            return null;
        } else {
            return topicMapper.topicsToTopicDTOsResponse(topic.get());
        }

    }

    @Override
    public List<TopicDTOResponse> findBasicTopics() {
        var topic = topicRepository.findByFatherIdIsNull();
        if (topic.isEmpty()) {
            //TODO
            return null;
        } else {
            return topicMapper.topicsToTopicDTOsResponse(topic.get());
        }
    }

    @Override
    public TopicDTOResponse findTopicByName(String topicName) {
        var topic = topicRepository.findByName(topicName);
        if (topic.isEmpty()) {
            //TODO
            return null;
        } else {
            return topicMapper.topicToTopicDTOResponse(topic.get());
        }

    }

    @Override
    public TopicDTOResponse findFatherByChild(Integer childId) {
        var topic = topicRepository.findFatherByChild(childId);
        if (topic.isEmpty()) {
            //TODO
            return null;
        } else {
            return topicMapper.topicToTopicDTOResponse(topic.get());
        }

    }
}
