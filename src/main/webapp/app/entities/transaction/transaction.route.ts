import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { TransactionComponent } from './transaction.component';
import { TransactionDetailComponent } from './transaction-detail.component';
import { TransactionPopupComponent } from './transaction-dialog.component';
import { TransactionDeletePopupComponent } from './transaction-delete-dialog.component';

export const transactionRoute: Routes = [
    {
        path: 'transaction',
        component: TransactionComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Transactions'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'transaction/:id',
        component: TransactionDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Transactions'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const transactionPopupRoute: Routes = [
    {
        path: 'transaction-new',
        component: TransactionPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Transactions'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'transaction/:id/edit',
        component: TransactionPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Transactions'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'transaction/:id/delete',
        component: TransactionDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Transactions'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
