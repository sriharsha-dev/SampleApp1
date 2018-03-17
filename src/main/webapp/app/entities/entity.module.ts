import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { SampleApp1ShopModule } from './shop/shop.module';
import { SampleApp1ItemsModule } from './items/items.module';
import { SampleApp1OptionsModule } from './options/options.module';
import { SampleApp1CustomerModule } from './customer/customer.module';
import { SampleApp1TransactionModule } from './transaction/transaction.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        SampleApp1ShopModule,
        SampleApp1ItemsModule,
        SampleApp1OptionsModule,
        SampleApp1CustomerModule,
        SampleApp1TransactionModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SampleApp1EntityModule {}
