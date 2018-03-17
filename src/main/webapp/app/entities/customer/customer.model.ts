import { BaseEntity } from './../../shared';

export class Customer implements BaseEntity {
    constructor(
        public id?: number,
        public uId?: number,
        public uLogin?: string,
        public uPassword?: string,
        public uTotalCredits?: number,
        public uPlacedCredits?: number,
        public uGainedCredits?: number,
        public uName?: string,
        public uContactInfo?: string,
        public createdOn?: any,
        public shop?: BaseEntity,
        public transactions?: BaseEntity[],
    ) {
    }
}
