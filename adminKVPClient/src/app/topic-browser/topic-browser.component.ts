import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { faTrash, faPen } from '@fortawesome/free-solid-svg-icons';
import { Topic } from '../models/topic';
import { TopicInfoService } from '../topic-service/topic-info.service';

@Component({
  selector: 'app-topic-browser',
  templateUrl: './topic-browser.component.html',
  styleUrls: ['./topic-browser.component.scss']
})
export class TopicBrowserComponent implements OnInit {

  public faTrash = faTrash;
  public faPen = faPen;

  public topics: Topic[] = [];

  constructor(private topicInfoService: TopicInfoService, private router: Router) { }

  async ngOnInit(): Promise<void> {
    await this.loadTopics();
  }

  async loadTopics() {
    this.topics = await this.topicInfoService.getTopics();
  }
  
  async deleteTopic(id: number) {
    await this.topicInfoService.deleteTopic(id);
    await this.loadTopics();
  }

  async editTopic(id: number) {
    this.router.navigate(['update', id])
  }


}
