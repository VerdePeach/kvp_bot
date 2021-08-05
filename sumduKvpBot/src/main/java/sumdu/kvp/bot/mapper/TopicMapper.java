package sumdu.kvp.bot.mapper;

import sumdu.kvp.bot.config.TopicResponseType;
import sumdu.kvp.bot.dto.TopicDTORequest;
import sumdu.kvp.bot.dto.TopicDTOResponse;
import sumdu.kvp.bot.model.Topic;

import java.util.List;
import java.util.stream.Collectors;

public class TopicMapper {

    public Topic topicDTORequestToTopic(TopicDTORequest topicDTORequest) {
        var topic = new Topic();
        topic.setId(topicDTORequest.getId());
        topic.setName(topicDTORequest.getName());
        topic.setText(topicDTORequest.getText());
        topic.setType(TopicResponseType.valueOf(topicDTORequest.getType().toUpperCase()));
//        topic.setImage(topicDTORequest.getImage());
//        topic.setFile(topicDTORequest.getFile());
//        topic.setSubTopics(topicDTORequest.getSubTopics());
        return topic;
    }

    public TopicDTOResponse topicToTopicDTOResponse(Topic topic) {
        var topicDTOResponse = new TopicDTOResponse();
        topicDTOResponse.setId(topic.getId());
        topicDTOResponse.setName(topic.getName());
        topicDTOResponse.setText(topic.getText());
//        topicDTOResponse.setImage(topic.getImage());
//        topicDTOResponse.setFile(topic.getFile());
//        topicDTOResponse.setSubTopics(topic.getSubTopics().stream().map(t -> topicToTopicDTOResponse(t))
//                .collect(Collectors.toList()));
        return topicDTOResponse;
    }

    public List<TopicDTOResponse> topicsToTopicDTOsResponse(List<Topic> topics) {
        return topics.stream().map(t -> topicToTopicDTOResponse(t)).collect(Collectors.toList());
    }
}
