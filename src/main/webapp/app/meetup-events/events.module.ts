import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {EventsRoutingModule} from './events-routing.module';
import {EventsListComponent} from "app/meetup-events/event-list/events-list.component";
import {EventEditComponent} from './event-edit/event-edit.component';
import {AcademyProjectSharedModule} from "app/shared";

@NgModule({
    declarations: [EventsListComponent, EventEditComponent],
    imports: [
        AcademyProjectSharedModule,
        CommonModule,
        EventsRoutingModule
    ]
})
export class EventsModule {
}
