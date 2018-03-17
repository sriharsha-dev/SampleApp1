import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Transaction } from './transaction.model';
import { TransactionService } from './transaction.service';
import { Principal } from '../../shared';

@Component({
    selector: 'jhi-transaction',
    templateUrl: './transaction.component.html'
})
export class TransactionComponent implements OnInit, OnDestroy {
transactions: Transaction[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private transactionService: TransactionService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.transactionService.query().subscribe(
            (res: HttpResponse<Transaction[]>) => {
                this.transactions = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInTransactions();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Transaction) {
        return item.id;
    }
    registerChangeInTransactions() {
        this.eventSubscriber = this.eventManager.subscribe('transactionListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
