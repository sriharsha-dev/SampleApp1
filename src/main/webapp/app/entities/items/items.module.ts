import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SampleApp1SharedModule } from '../../shared';
import {
    ItemsService,
    ItemsPopupService,
    ItemsComponent,
    ItemsDetailComponent,
    ItemsDialogComponent,
    ItemsPopupComponent,
    ItemsDeletePopupComponent,
    ItemsDeleteDialogComponent,
    itemsRoute,
    itemsPopupRoute,
} from './';

const ENTITY_STATES = [
    ...itemsRoute,
    ...itemsPopupRoute,
];

@NgModule({
    imports: [
        SampleApp1SharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        ItemsComponent,
        ItemsDetailComponent,
        ItemsDialogComponent,
        ItemsDeleteDialogComponent,
        ItemsPopupComponent,
        ItemsDeletePopupComponent,
    ],
    entryComponents: [
        ItemsComponent,
        ItemsDialogComponent,
        ItemsPopupComponent,
        ItemsDeleteDialogComponent,
        ItemsDeletePopupComponent,
    ],
    providers: [
        ItemsService,
        ItemsPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SampleApp1ItemsModule {}
