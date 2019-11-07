import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs/internal/Observable";
import {SERVER_API_URL} from "app/app.constants";

@Injectable({
    providedIn: 'root'
})
export class MeetupChallengesService {

    constructor(private http: HttpClient) {
    }

    //TODO: sort the array by method scope
    getFutureChallenges(id: number): Observable<any> {
        return this.getAllChallengesByUserId(id);
    }

    getMyChallenges(id: number): Observable<any> {
        return this.http.get(SERVER_API_URL + "challenge/creator/" + id, {observe: 'response'});
}

    //TODO: sort the array by method scope
    getPendingChallenges(id: number): Observable<any> {
        return this.getAllChallengesByUserId(id);
    }

    getChallengeById(id: number): Observable<any> {
        return this.http.get(SERVER_API_URL + "challenge/" + id, {observe: 'response'});
    }

    getAllChallengesByUserId(id: number): Observable<any> {
        return this.http.get(SERVER_API_URL + "challenge/user/" + id, {observe: 'response'});
    }

    deleteChallenge(id: number): Observable<any> {
        return this.http.delete(SERVER_API_URL + "/challenge/" + id, {observe: 'response'});
    }
}
