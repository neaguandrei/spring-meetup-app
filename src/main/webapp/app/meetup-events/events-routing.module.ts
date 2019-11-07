import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {EventsListComponent} from "app/meetup-events/event-list/events-list.component";
import {EventEditComponent} from "app/meetup-events/event-edit/event-edit.component";

const routes: Routes = [
    {
        path: '',
        redirectTo: 'list',
        pathMatch:'full'
    },
    {
        path: 'list',
        component: EventsListComponent
    },
    {
        path: 'edit/:id',
        component: EventEditComponent
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class EventsRoutingModule {
}
