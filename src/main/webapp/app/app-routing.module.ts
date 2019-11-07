import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';
import {errorRoute, JhiMainComponent, navbarRoute} from './layouts';
import {DEBUG_INFO_ENABLED} from 'app/app.constants';
import {MeetupChallengesModule} from "app/meetup-challenges/meetup-challenges.module";

const LAYOUT_ROUTES = [navbarRoute, ...errorRoute];

@NgModule({
    imports: [
        RouterModule.forRoot(
            [
                {
                    path: 'admin',
                    loadChildren: './admin/admin.module#AcademyProjectAdminModule'
                },
                {
                    path: 'meetup',
                    loadChildren: './meetup-user/meetup.module#MeetupModule'
                },
                {
                    path: 'events',
                    loadChildren: './meetup-events/events.module#EventsModule'
                },
                {
                    path: 'challenges',
                    loadChildren: './meetup-challenges/meetup-challenges.module#MeetupChallengesModule'
                },
                ...LAYOUT_ROUTES
            ],
            {enableTracing: DEBUG_INFO_ENABLED}
        )
    ],
    exports: [RouterModule]
})
export class AcademyProjectAppRoutingModule {
}
