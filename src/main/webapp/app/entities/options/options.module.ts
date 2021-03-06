import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SampleApp1SharedModule } from '../../shared';
import {
    OptionsService,
    OptionsPopupService,
    OptionsComponent,
    OptionsDetailComponent,
    OptionsDialogComponent,
    OptionsPopupComponent,
    OptionsDeletePopupComponent,
    OptionsDeleteDialogComponent,
    optionsRoute,
    optionsPopupRoute,
} from './';

const ENTITY_STATES = [
    ...optionsRoute,
    ...optionsPopupRoute,
];

@NgModule({
    imports: [
        SampleApp1SharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        OptionsComponent,
        OptionsDetailComponent,
        OptionsDialogComponent,
        OptionsDeleteDialogComponent,
        OptionsPopupComponent,
        OptionsDeletePopupComponent,
    ],
    entryComponents: [
        OptionsComponent,
        OptionsDialogComponent,
        OptionsPopupComponent,
        OptionsDeleteDialogComponent,
        OptionsDeletePopupComponent,
    ],
    providers: [
        OptionsService,
        OptionsPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SampleApp1OptionsModule {}
