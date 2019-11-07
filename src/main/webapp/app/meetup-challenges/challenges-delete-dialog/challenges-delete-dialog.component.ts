import {Component, Input, OnInit} from '@angular/core';
import {MeetupChallenge} from "app/meetup-challenges/model/MeetupChallenge";
import {JhiEventManager} from "ng-jhipster";
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {MeetupChallengesService} from "app/meetup-challenges/meetup-challenges.service";

@Component({
    selector: 'jhi-challenges-delete-dialog',
    templateUrl: './challenges-delete-dialog.component.html',
    styleUrls: ['./challenges-delete-dialog.component.scss']
})
export class ChallengesDeleteDialogComponent implements OnInit {

    challenge: MeetupChallenge;

    constructor(public activeModal: NgbActiveModal,
                private eventManager: JhiEventManager,
                private meetupService: MeetupChallengesService) {
    }

    ngOnInit() {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete() {
        this.meetupService.deleteChallenge(this.challenge.id).subscribe(() => {
            this.clear();
        })
    }

}
