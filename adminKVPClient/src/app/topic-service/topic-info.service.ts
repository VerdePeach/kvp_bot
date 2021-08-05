import { Injectable } from '@angular/core';
import { Topic } from '../models/topic';
import { TopicDTORequest } from '../models/topicDTORequest';
import { TopicService } from './topic.service';

@Injectable({
  providedIn: 'root'
})
export class TopicInfoService {

  constructor(private topicService: TopicService) { }

  async getTopics(): Promise<Topic[]> {
    return await this.topicService.getTopics().toPromise();
  }

  async createTopic(topic: TopicDTORequest): Promise<Topic> {
    if(topic.id) {
      return await this.topicService.updateTopic(topic).toPromise();
    } else {
      return await this.topicService.saveTopic(topic).toPromise();
    }
  }

  async deleteTopic(topicId: number): Promise<void> {
    await this.topicService.deleteTopic(topicId).toPromise();
  }
}
