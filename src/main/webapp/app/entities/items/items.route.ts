import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { ItemsComponent } from './items.component';
import { ItemsDetailComponent } from './items-detail.component';
import { ItemsPopupComponent } from './items-dialog.component';
import { ItemsDeletePopupComponent } from './items-delete-dialog.component';

export const itemsRoute: Routes = [
    {
        path: 'items',
        component: ItemsComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Items'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'items/:id',
        component: ItemsDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Items'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const itemsPopupRoute: Routes = [
    {
        path: 'items-new',
        component: ItemsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Items'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'items/:id/edit',
        component: ItemsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Items'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'items/:id/delete',
        component: ItemsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Items'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
