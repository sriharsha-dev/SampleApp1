import { BaseEntity } from './../../shared';

export class Options implements BaseEntity {
    constructor(
        public id?: number,
        public opId?: number,
        public name?: string,
        public ratio?: number,
        public items?: BaseEntity,
    ) {
    }
}
