import { BaseEntity } from './../../shared';

export class Shop implements BaseEntity {
    constructor(
        public id?: number,
        public sId?: number,
        public login?: string,
        public password?: string,
        public name?: string,
        public customers?: BaseEntity[],
        public items?: BaseEntity[],
    ) {
    }
}
