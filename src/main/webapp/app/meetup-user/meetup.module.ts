import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { MeetupRoutingModule } from './meetup-routing.module';
import { MeetupComponent } from './meetup-main/meetup.component';
import { UserProfileComponent } from 'app/meetup-user/user-profile/edit/user-profile.component';
import { MyProfileComponent } from './user-profile/my-profile/my-profile.component';
import {AcademyProjectSharedCommonModule} from "app/shared";

@NgModule({
  declarations: [MeetupComponent, UserProfileComponent, MyProfileComponent],
  imports: [CommonModule, MeetupRoutingModule, AcademyProjectSharedCommonModule]
})
export class MeetupModule {}
