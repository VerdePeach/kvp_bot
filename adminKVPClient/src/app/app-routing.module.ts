import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { GenerateTopicComponent } from './generate-topic/generate-topic.component';
import { TopicBrowserComponent } from './topic-browser/topic-browser.component';

const routes: Routes = [
  { path: 'browser', component: TopicBrowserComponent },
  { path: 'generate', component: GenerateTopicComponent },
  { path: 'update/:id', component: GenerateTopicComponent },
  { path: '**', component: TopicBrowserComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
