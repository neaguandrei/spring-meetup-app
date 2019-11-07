import {User} from "app/core";
import {ChallengeCategory} from "app/meetup-challenges/model/ChallengeCategory";

export class MeetupChallenge {
    id: number;
    name: string;
    creator: User;
    startDate: Date;
    endDate: Date;
    status: string;
    points: number;
    description: string;
    challengeCategory: ChallengeCategory;
    active: boolean;

}