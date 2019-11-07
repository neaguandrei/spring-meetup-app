import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {ChallengesListComponent} from "app/meetup-challenges/challenges-list/challenges-list.component";
import {ChallengesEditComponent} from "app/meetup-challenges/challenges-edit/challenges-edit.component";
import {JhiResolvePagingParams} from "ng-jhipster";

const routes: Routes = [
    {
        path: '',
        redirectTo: 'list',
        pathMatch: 'full'
    },
    {
        path: 'list',
        component: ChallengesListComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            defaultSort: 'startDate,asc'
        }
    },
    {
        path: 'edit/:id',
        component: ChallengesEditComponent
    },
    {
        path: 'add',
        component: ChallengesEditComponent
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class MeetupChallengesRoutingModule {
}
