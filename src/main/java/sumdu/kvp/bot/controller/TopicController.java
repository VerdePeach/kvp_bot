package sumdu.kvp.bot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sumdu.kvp.bot.dto.TopicDTORequest;
import sumdu.kvp.bot.dto.TopicDTOResponse;
import sumdu.kvp.bot.service.TopicService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/topics")
public class TopicController {

    private TopicService topicService;

    @Autowired
    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @PostMapping
    public ResponseEntity<TopicDTOResponse> createTopic(@Valid @RequestBody TopicDTORequest topicDTORequest, BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity(result.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(topicService.create(topicDTORequest), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TopicDTOResponse>> getTopics() {
        return new ResponseEntity<>(topicService.findAll(), HttpStatus.CREATED);
    }
}
