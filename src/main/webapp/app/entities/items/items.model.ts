import { BaseEntity } from './../../shared';

export class Items implements BaseEntity {
    constructor(
        public id?: number,
        public itemId?: number,
        public createdBy?: BaseEntity,
        public name?: string,
        public createdOn?: any,
        public totalPlacedCredits?: number,
        public status?: boolean,
        public commission?: number,
        public shop?: BaseEntity,
        public finalOption?: BaseEntity,
        public options?: BaseEntity[],
    ) {
        this.status = false;
    }
}
