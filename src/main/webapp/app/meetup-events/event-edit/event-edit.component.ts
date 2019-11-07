import {Component, OnInit} from '@angular/core';
import {MeetupEvent} from "app/meetup-events/model/MeetupEvent";
import {AccountService, IUser, User, UserService} from "app/core";
import {ActivatedRoute} from "@angular/router";
import {EventsService} from "app/meetup-events/events.service";

@Component({
    selector: 'jhi-event-edit',
    templateUrl: './event-edit.component.html',
    styleUrls: ['./event-edit.component.scss']
})
export class EventEditComponent implements OnInit {

    meetupEvent: MeetupEvent = null;
    meetupId: number = null;
    isOwner: boolean;

    constructor(private accountService: AccountService,
                private route: ActivatedRoute,
                private eventService: EventsService) {}

    ngOnInit() {
        this.route.params.subscribe(params => {
            this.meetupId = params['id'];
        });
        this.accountService.identity().then((userIdentity) => {
            this.isOwner = userIdentity.login === this.meetupEvent.owner.login;
            this.initEvent();
        })
    }

    initEvent() {
        this.eventService.getEventById(this.meetupId).subscribe((data)=>{
            this.meetupEvent = data.body;
        })
    }

}
