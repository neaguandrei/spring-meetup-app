import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {AccountService, LoginModalService, LoginService} from "app/core";
import {Router} from "@angular/router";
import {ProfileService} from "app/layouts/profiles/profile.service";
import {NgbModalRef} from "@ng-bootstrap/ng-bootstrap";

@Component({
    selector: 'jhi-sidebar',
    templateUrl: './sidebar.component.html',
    styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent implements OnInit {

    @Output() isMenuExpanded: EventEmitter<any> = new EventEmitter<any>();

    isExpanded = true;
    inProduction: boolean;
    swaggerEnabled: boolean;
    modalRef: NgbModalRef;
    version: string;


    constructor(private loginService: LoginService,private accountService: AccountService,
                private loginModalService: LoginModalService,
                private profileService: ProfileService,
                private router: Router) {
    }

    ngOnInit() {
        this.profileService.getProfileInfo().then(profileInfo => {
            this.inProduction = profileInfo.inProduction;
            this.swaggerEnabled = profileInfo.swaggerEnabled;
        });
    }

    toggleMenu() {
        this.isExpanded = !this.isExpanded;
        this.isMenuExpanded.emit({isExpanded: this.isExpanded});
    }

    isAuthenticated() {
        return this.accountService.isAuthenticated();
    }

    login() {
        this.modalRef = this.loginModalService.open();
    }

    logout() {
        this.loginService.logout();
        this.router.navigate(['']);
    }
}
