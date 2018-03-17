import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Items } from './items.model';
import { ItemsPopupService } from './items-popup.service';
import { ItemsService } from './items.service';
import { Shop, ShopService } from '../shop';
import { Options, OptionsService } from '../options';

@Component({
    selector: 'jhi-items-dialog',
    templateUrl: './items-dialog.component.html'
})
export class ItemsDialogComponent implements OnInit {

    items: Items;
    isSaving: boolean;

    shops: Shop[];

    finaloptions: Options[];

    createdbies: Shop[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private itemsService: ItemsService,
        private shopService: ShopService,
        private optionsService: OptionsService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.shopService.query()
            .subscribe((res: HttpResponse<Shop[]>) => { this.shops = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.optionsService
            .query({filter: 'items-is-null'})
            .subscribe((res: HttpResponse<Options[]>) => {
                if (!this.items.finalOption || !this.items.finalOption.id) {
                    this.finaloptions = res.body;
                } else {
                    this.optionsService
                        .find(this.items.finalOption.id)
                        .subscribe((subRes: HttpResponse<Options>) => {
                            this.finaloptions = [subRes.body].concat(res.body);
                        }, (subRes: HttpErrorResponse) => this.onError(subRes.message));
                }
            }, (res: HttpErrorResponse) => this.onError(res.message));
        this.shopService
            .query({filter: 'items-is-null'})
            .subscribe((res: HttpResponse<Shop[]>) => {
                if (!this.items.createdBy || !this.items.createdBy.id) {
                    this.createdbies = res.body;
                } else {
                    this.shopService
                        .find(this.items.createdBy.id)
                        .subscribe((subRes: HttpResponse<Shop>) => {
                            this.createdbies = [subRes.body].concat(res.body);
                        }, (subRes: HttpErrorResponse) => this.onError(subRes.message));
                }
            }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.items.id !== undefined) {
            this.subscribeToSaveResponse(
                this.itemsService.update(this.items));
        } else {
            this.subscribeToSaveResponse(
                this.itemsService.create(this.items));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Items>>) {
        result.subscribe((res: HttpResponse<Items>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Items) {
        this.eventManager.broadcast({ name: 'itemsListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackShopById(index: number, item: Shop) {
        return item.id;
    }

    trackOptionsById(index: number, item: Options) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-items-popup',
    template: ''
})
export class ItemsPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private itemsPopupService: ItemsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.itemsPopupService
                    .open(ItemsDialogComponent as Component, params['id']);
            } else {
                this.itemsPopupService
                    .open(ItemsDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
