import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {UserProfileComponent} from 'app/meetup-user/user-profile/edit/user-profile.component';
import {MyProfileComponent} from "app/meetup-user/user-profile/my-profile/my-profile.component";

const routes: Routes = [
    {
        path: '',
        children: [
            {
                path: 'user-profile',
                component: MyProfileComponent,
                data: {
                    pageTitle: 'My Profile'
                }
            },
            {
                path: 'edit',
                component: UserProfileComponent,
                data: {
                    pageTitle: 'My profile'
                }
            }
        ],
        data: {
            authorities: [],
            pageTitle: 'Meet UP!'
        }
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class MeetupRoutingModule {
}
