import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { ShopComponent } from './shop.component';
import { ShopDetailComponent } from './shop-detail.component';
import { ShopPopupComponent } from './shop-dialog.component';
import { ShopDeletePopupComponent } from './shop-delete-dialog.component';

export const shopRoute: Routes = [
    {
        path: 'shop',
        component: ShopComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Shops'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'shop/:id',
        component: ShopDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Shops'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const shopPopupRoute: Routes = [
    {
        path: 'shop-new',
        component: ShopPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Shops'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'shop/:id/edit',
        component: ShopPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Shops'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'shop/:id/delete',
        component: ShopDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Shops'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
