import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Transaction } from './transaction.model';
import { TransactionPopupService } from './transaction-popup.service';
import { TransactionService } from './transaction.service';
import { Customer, CustomerService } from '../customer';
import { Options, OptionsService } from '../options';
import { Items, ItemsService } from '../items';

@Component({
    selector: 'jhi-transaction-dialog',
    templateUrl: './transaction-dialog.component.html'
})
export class TransactionDialogComponent implements OnInit {

    transaction: Transaction;
    isSaving: boolean;

    customers: Customer[];

    placedbies: Customer[];

    options: Options[];

    items: Items[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private transactionService: TransactionService,
        private customerService: CustomerService,
        private optionsService: OptionsService,
        private itemsService: ItemsService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.customerService.query()
            .subscribe((res: HttpResponse<Customer[]>) => { this.customers = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.customerService
            .query({filter: 'transaction-is-null'})
            .subscribe((res: HttpResponse<Customer[]>) => {
                if (!this.transaction.placedBy || !this.transaction.placedBy.id) {
                    this.placedbies = res.body;
                } else {
                    this.customerService
                        .find(this.transaction.placedBy.id)
                        .subscribe((subRes: HttpResponse<Customer>) => {
                            this.placedbies = [subRes.body].concat(res.body);
                        }, (subRes: HttpErrorResponse) => this.onError(subRes.message));
                }
            }, (res: HttpErrorResponse) => this.onError(res.message));
        this.optionsService
            .query({filter: 'transaction-is-null'})
            .subscribe((res: HttpResponse<Options[]>) => {
                if (!this.transaction.option || !this.transaction.option.id) {
                    this.options = res.body;
                } else {
                    this.optionsService
                        .find(this.transaction.option.id)
                        .subscribe((subRes: HttpResponse<Options>) => {
                            this.options = [subRes.body].concat(res.body);
                        }, (subRes: HttpErrorResponse) => this.onError(subRes.message));
                }
            }, (res: HttpErrorResponse) => this.onError(res.message));
        this.itemsService
            .query({filter: 'transaction-is-null'})
            .subscribe((res: HttpResponse<Items[]>) => {
                if (!this.transaction.item || !this.transaction.item.id) {
                    this.items = res.body;
                } else {
                    this.itemsService
                        .find(this.transaction.item.id)
                        .subscribe((subRes: HttpResponse<Items>) => {
                            this.items = [subRes.body].concat(res.body);
                        }, (subRes: HttpErrorResponse) => this.onError(subRes.message));
                }
            }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.transaction.id !== undefined) {
            this.subscribeToSaveResponse(
                this.transactionService.update(this.transaction));
        } else {
            this.subscribeToSaveResponse(
                this.transactionService.create(this.transaction));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Transaction>>) {
        result.subscribe((res: HttpResponse<Transaction>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Transaction) {
        this.eventManager.broadcast({ name: 'transactionListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackCustomerById(index: number, item: Customer) {
        return item.id;
    }

    trackOptionsById(index: number, item: Options) {
        return item.id;
    }

    trackItemsById(index: number, item: Items) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-transaction-popup',
    template: ''
})
export class TransactionPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private transactionPopupService: TransactionPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.transactionPopupService
                    .open(TransactionDialogComponent as Component, params['id']);
            } else {
                this.transactionPopupService
                    .open(TransactionDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
