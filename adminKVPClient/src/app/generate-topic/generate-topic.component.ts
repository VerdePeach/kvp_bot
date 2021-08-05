import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Topic } from '../models/topic';
import { TopicInfoService } from '../topic-service/topic-info.service';

@Component({
  selector: 'app-generate-topic',
  templateUrl: './generate-topic.component.html',
  styleUrls: ['./generate-topic.component.scss']
})
export class GenerateTopicComponent implements OnInit {

  public showInputError = false;

  public topicName = '';
  public topicText = '';
  public fatherTopicId = '';

  public topics: Topic[] = [];
  public topic: Topic | undefined;
  public saveSuccess = false;

  constructor(private topicInfoService: TopicInfoService,  private route: ActivatedRoute) { }

  async ngOnInit(): Promise<void> {
    this.topics = await this.topicInfoService.getTopics();
    this.topic = this.topics.filter((t) => t.id == this.route.snapshot.params['id'])?.pop()
    if (this.topic) {
      this.topicName = this.topic.name;
      this.topicText = this.topic.text;
    }
  }

  async saveTopic(): Promise<void> {
    if (this.topicName && this.topicText) {
      this.showInputError = false;
      await this.topicInfoService.createTopic({
        id: this.topic?.id || NaN,
        name: this.topicName,
        text: this.topicText,
        type: 'MESSAGE',
        fatherId: this.fatherTopicId ? Number(this.fatherTopicId) : NaN
      });
      this.saveSuccess = true;
    } else {
      this.showInputError = true;
    }
    
  }

}
