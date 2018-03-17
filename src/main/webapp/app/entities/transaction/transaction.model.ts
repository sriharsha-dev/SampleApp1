import { BaseEntity } from './../../shared';

export class Transaction implements BaseEntity {
    constructor(
        public id?: number,
        public tId?: number,
        public placedCredits?: number,
        public placedOn?: any,
        public customer?: BaseEntity,
        public placedBy?: BaseEntity,
        public option?: BaseEntity,
        public item?: BaseEntity,
    ) {
    }
}
