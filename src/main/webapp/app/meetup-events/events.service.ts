import {Injectable} from '@angular/core';
import {Observable} from "rxjs/internal/Observable";
import {HttpClient} from "@angular/common/http";
import {SERVER_API_URL} from "app/app.constants";

@Injectable({
    providedIn: 'root'
})
export class EventsService {

    constructor(private http: HttpClient) {
    }

    //TODO: sort the array by method scope or call specific API
    getFutureEvents(id: number): Observable<any> {
        return this.getAllEventsByUserId(id);
    }

    //TODO: sort the array by method scope or call specific API
    getMyEvents(id: number): Observable<any> {
        return this.getAllEventsByUserId(id);
    }

    //TODO: sort the array by method scope or call specific API
    getPendingEvents(id: number): Observable<any> {
        return this.getAllEventsByUserId(id);
    }

    //TODO: call the API
    getEventById(id: number): Observable<any> {
        return null;
    }

    getAllEventsByUserId(id: number): Observable<any> {
        return this.http.get(SERVER_API_URL + "event/user/" + id, {observe: 'response'});
    }
}
