import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Shop } from './shop.model';
import { ShopPopupService } from './shop-popup.service';
import { ShopService } from './shop.service';

@Component({
    selector: 'jhi-shop-dialog',
    templateUrl: './shop-dialog.component.html'
})
export class ShopDialogComponent implements OnInit {

    shop: Shop;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private shopService: ShopService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.shop.id !== undefined) {
            this.subscribeToSaveResponse(
                this.shopService.update(this.shop));
        } else {
            this.subscribeToSaveResponse(
                this.shopService.create(this.shop));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Shop>>) {
        result.subscribe((res: HttpResponse<Shop>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Shop) {
        this.eventManager.broadcast({ name: 'shopListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-shop-popup',
    template: ''
})
export class ShopPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private shopPopupService: ShopPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.shopPopupService
                    .open(ShopDialogComponent as Component, params['id']);
            } else {
                this.shopPopupService
                    .open(ShopDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
