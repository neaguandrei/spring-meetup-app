import {Component, OnInit} from '@angular/core';
import {MeetupChallenge} from "app/meetup-challenges/model/MeetupChallenge";
import {AccountService, IUser, User, UserService} from "app/core";
import {ActivatedRoute, Router} from "@angular/router";
import {MeetupChallengesService} from "app/meetup-challenges/meetup-challenges.service";
import {UserMgmtDeleteDialogComponent} from "app/admin/user-management/user-management-delete-dialog.component";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {ChallengesDeleteDialogComponent} from "app/meetup-challenges/challenges-delete-dialog/challenges-delete-dialog.component";

@Component({
    selector: 'jhi-challenges-list',
    templateUrl: './challenges-list.component.html',
    styleUrls: ['./challenges-list.component.scss']
})
export class ChallengesListComponent implements OnInit {

    challenges: MeetupChallenge[];
    isFutureChallengesActive: boolean = true;
    isMyChallengesActive: boolean;
    isPendingActive: boolean;
    user: IUser;
    selectedChallenge: MeetupChallenge;
    predicate: any;
    reverse: any;
    routeData: any;

    constructor(private service: MeetupChallengesService,
                private router: Router,
                private accountService: AccountService,
                private  userService: UserService,
                private modalService: NgbModal,
                private activatedRoute: ActivatedRoute) {
    }

    ngOnInit() {
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.reverse = data['pagingParams'].ascending;
            this.predicate = data['pagingParams'].predicate;
        });
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
        this.getChallenges();
    }

    getChallenges() {
        if (this.isFutureChallengesActive) {
            this.service.getFutureChallenges(this.user.id).subscribe((response) => {
                if (response) {
                    this.challenges = response.body;
                }
            }, () => {

            });
        } else if (this.isPendingActive) {
            this.service.getPendingChallenges(this.user.id).subscribe((response) => {
                if (response) {
                    this.challenges = response.body;
                }
            }, () => {

            });
        } else {
            this.service.getMyChallenges(this.user.id).subscribe((response) => {
                if (response) {
                    this.challenges = response.body
                }
            }, () => {

            });
        }
    }

    setTabFutureChallenges() {
        if (!this.isFutureChallengesActive) {
            this.isFutureChallengesActive = true;
            this.isPendingActive = false;
            this.isMyChallengesActive = false;
        }
    }

    setTabMyChallenges() {
        if (!this.isMyChallengesActive) {
            this.isMyChallengesActive = true;
            this.isFutureChallengesActive = false;
            this.isPendingActive = false;
        }
    }


    setTabPendingChallenges() {
        if (!this.isPendingActive) {
            this.isPendingActive = true;
            this.isFutureChallengesActive = false;
            this.isMyChallengesActive = false;
        }
    }

    onChallengeClick(challenge) {
        if(this.selectedChallenge && this.selectedChallenge.id!==challenge.id){
            this.selectedChallenge.active=false;
            this.selectedChallenge=challenge;
            this.selectedChallenge.active=true;
        }
    }

    goToEdit(){
        this.router.navigate(['challenges/edit/' + this.selectedChallenge.id]);
    }

    deleteChallenge() {
        const modalRef = this.modalService.open(ChallengesDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
        modalRef.componentInstance.challenge = this.selectedChallenge;
        modalRef.result.then(
            result => {
                // Left blank intentionally, nothing to do here
            },
            reason => {
                // Left blank intentionally, nothing to do here
            }
        );
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    transition() {
        this.router.navigate(['/challenges/list'], {
            queryParams: {
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.getCurrentUser();
    }

}
