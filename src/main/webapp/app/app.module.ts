import './vendor.ts';

import {NgModule, NO_ERRORS_SCHEMA} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {HTTP_INTERCEPTORS} from '@angular/common/http';
import {NgbDatepickerConfig} from '@ng-bootstrap/ng-bootstrap';
import {NgxWebstorageModule} from 'ngx-webstorage';
import {NgJhipsterModule} from 'ng-jhipster';

import {AuthInterceptor} from './blocks/interceptor/auth.interceptor';
import {AuthExpiredInterceptor} from './blocks/interceptor/auth-expired.interceptor';
import {ErrorHandlerInterceptor} from './blocks/interceptor/errorhandler.interceptor';
import {NotificationInterceptor} from './blocks/interceptor/notification.interceptor';
import {AcademyProjectSharedModule} from 'app/shared';
import {AcademyProjectCoreModule} from 'app/core';
import {AcademyProjectAppRoutingModule} from './app-routing.module';
import {AcademyProjectHomeModule} from './home/home.module';
import {AcademyProjectAccountModule} from './account/account.module';
import {AcademyProjectEntityModule} from './entities/entity.module';
import * as moment from 'moment';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import {ErrorComponent, FooterComponent, JhiMainComponent, NavbarComponent, PageRibbonComponent} from './layouts';
import {SidebarComponent} from './layouts/navbar/sidebar/sidebar.component';
import {MDBBootstrapModule} from "angular-bootstrap-md";

@NgModule({
    imports: [
        BrowserModule,
        NgxWebstorageModule.forRoot({prefix: 'jhi', separator: '-'}),
        NgJhipsterModule.forRoot({
            // set below to true to make alerts look like toast
            alertAsToast: false,
            alertTimeout: 5000
        }),
        AcademyProjectSharedModule.forRoot(),
        AcademyProjectCoreModule,
        AcademyProjectHomeModule,
        AcademyProjectAccountModule,
        // jhipster-needle-angular-add-module JHipster will add new module here
        AcademyProjectEntityModule,
        AcademyProjectAppRoutingModule,
        MDBBootstrapModule.forRoot()
    ],
    declarations: [JhiMainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent, SidebarComponent],
    providers: [
        {
            provide: HTTP_INTERCEPTORS,
            useClass: AuthInterceptor,
            multi: true
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: AuthExpiredInterceptor,
            multi: true
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: ErrorHandlerInterceptor,
            multi: true
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: NotificationInterceptor,
            multi: true
        }
    ],
    bootstrap: [JhiMainComponent],
    schemas: [ NO_ERRORS_SCHEMA ]
})
export class AcademyProjectAppModule {
    constructor(private dpConfig: NgbDatepickerConfig) {
        this.dpConfig.minDate = {year: moment().year() - 100, month: 1, day: 1};
    }
}
