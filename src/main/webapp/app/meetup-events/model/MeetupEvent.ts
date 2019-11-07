import {User} from "app/core";

export class MeetupEvent{
    id: number;
    name: string;
    description: string;
    startDate: Date;
    endDate: Date;
    owner: User;
    notes: string;
    isPublic: boolean;
    address: string;


    constructor(id: number, name: string, description: string, startDate: Date, endDate: Date, owner: User, notes: string, isPublic: boolean, address: string) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.owner = owner;
        this.notes = notes;
        this.isPublic = isPublic;
        this.address = address;
    }
}