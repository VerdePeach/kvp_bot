import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { Topic } from '../models/topic';
import { TopicDTORequest } from '../models/topicDTORequest';

@Injectable({
  providedIn: 'root'
})
export class TopicService {
  private url = "http://localhost:5000"
  private allTopicsEndpoint = "/topics"
  
  constructor(private http: HttpClient) { }

  getTopics(): Observable<Topic[]> {
    return this.http.get<Topic[]>(this.url + this.allTopicsEndpoint);
  }

  saveTopic(topic: TopicDTORequest): Observable<Topic> {
    return this.http.post<TopicDTORequest>(this.url + this.allTopicsEndpoint, topic);
  }

  updateTopic(topic: TopicDTORequest): Observable<Topic> {
    return this.http.put<TopicDTORequest>(this.url + this.allTopicsEndpoint + '/' + topic.id, topic);
  }

  deleteTopic(topicId: number): Observable<void> {
    return this.http.delete<void>(this.url + this.allTopicsEndpoint + '/' + topicId);
  }
}
