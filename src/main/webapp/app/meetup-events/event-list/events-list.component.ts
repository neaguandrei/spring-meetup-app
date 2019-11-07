import {Component, OnInit} from '@angular/core';
import {MeetupEvent} from "app/meetup-events/model/MeetupEvent";
import {EventsService} from "app/meetup-events/events.service";
import {Router} from "@angular/router";
import {AccountService, IUser, UserService} from "app/core";

@Component({
    selector: 'jhi-events-list',
    templateUrl: './events-list.component.html',
    styleUrls: ['./events-list.component.scss']
})
export class EventsListComponent implements OnInit {

    events: MeetupEvent[] = [];
    isFutureEventsActive: boolean;
    isMyEventsActive: boolean;
    isGetPendingActive: boolean;
    user: IUser;

    constructor(private service: EventsService,
                private router: Router,
                private accountService: AccountService,
                private  userService: UserService) {
    }

    ngOnInit() {
        this.getCurrentUser();
    }

    getCurrentUser() {
        return this.accountService.identity().then((userIdentity) => {
            this.userService.find(userIdentity.login).toPromise()
                .then(
                    response => this.onUserFound(response.body));
        })
    }

    onUserFound(data) {
        this.user = data;
        this.getEvents();
    }

    getEvents() {
        if (this.isFutureEventsActive) {
            this.service.getFutureEvents(this.user.id).subscribe((response) => {
                if (response) {
                    this.events = response.body;
                }
            }, () => {

            });
        } else if (this.isGetPendingActive) {
            this.service.getPendingEvents(this.user.id).subscribe((response) => {
                if (response) {
                    this.events = response.body;
                }
            }, () => {

            });
        } else {
            this.service.getMyEvents(this.user.id).subscribe((response) => {
                if (response) {
                    this.events = response.body
                }
            }, () => {

            });
        }
    }

    setTabFutureEvents() {
        if (!this.isFutureEventsActive) {
            this.isFutureEventsActive = true;
            this.isGetPendingActive = false;
            this.isMyEventsActive = false;
        }
    }

    setTabMyEvents() {
        if (!this.isMyEventsActive) {
            this.isMyEventsActive = true;
            this.isFutureEventsActive = false;
            this.isGetPendingActive = false;
        }
    }


    setTabPendingEvents() {
        if (!this.isGetPendingActive) {
            this.isGetPendingActive = true;
            this.isFutureEventsActive = false;
            this.isMyEventsActive = false;
        }
    }

    onEventClick(event) {
        this.router.navigate(['events/edit/' + event.id]);
    }
}
