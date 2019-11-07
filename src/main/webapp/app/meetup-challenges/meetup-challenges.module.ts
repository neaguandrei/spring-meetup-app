import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {MeetupChallengesRoutingModule} from './meetup-challenges-routing.module';
import {ChallengesListComponent} from './challenges-list/challenges-list.component';
import {ChallengesEditComponent} from './challenges-edit/challenges-edit.component';
import { ChallengesDeleteDialogComponent } from './challenges-delete-dialog/challenges-delete-dialog.component';
import {AcademyProjectSharedModule} from "app/shared";

@NgModule({
    declarations: [ChallengesListComponent, ChallengesEditComponent, ChallengesDeleteDialogComponent],
    imports: [
        AcademyProjectSharedModule,
        CommonModule,
        MeetupChallengesRoutingModule
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MeetupChallengesModule {
}
