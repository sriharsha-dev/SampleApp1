import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SampleApp1SharedModule } from '../../shared';
import {
    ShopService,
    ShopPopupService,
    ShopComponent,
    ShopDetailComponent,
    ShopDialogComponent,
    ShopPopupComponent,
    ShopDeletePopupComponent,
    ShopDeleteDialogComponent,
    shopRoute,
    shopPopupRoute,
} from './';

const ENTITY_STATES = [
    ...shopRoute,
    ...shopPopupRoute,
];

@NgModule({
    imports: [
        SampleApp1SharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        ShopComponent,
        ShopDetailComponent,
        ShopDialogComponent,
        ShopDeleteDialogComponent,
        ShopPopupComponent,
        ShopDeletePopupComponent,
    ],
    entryComponents: [
        ShopComponent,
        ShopDialogComponent,
        ShopPopupComponent,
        ShopDeleteDialogComponent,
        ShopDeletePopupComponent,
    ],
    providers: [
        ShopService,
        ShopPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SampleApp1ShopModule {}
