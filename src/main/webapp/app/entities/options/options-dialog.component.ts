import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Options } from './options.model';
import { OptionsPopupService } from './options-popup.service';
import { OptionsService } from './options.service';
import { Items, ItemsService } from '../items';

@Component({
    selector: 'jhi-options-dialog',
    templateUrl: './options-dialog.component.html'
})
export class OptionsDialogComponent implements OnInit {

    options: Options;
    isSaving: boolean;

    items: Items[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private optionsService: OptionsService,
        private itemsService: ItemsService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.itemsService.query()
            .subscribe((res: HttpResponse<Items[]>) => { this.items = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.options.id !== undefined) {
            this.subscribeToSaveResponse(
                this.optionsService.update(this.options));
        } else {
            this.subscribeToSaveResponse(
                this.optionsService.create(this.options));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Options>>) {
        result.subscribe((res: HttpResponse<Options>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Options) {
        this.eventManager.broadcast({ name: 'optionsListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackItemsById(index: number, item: Items) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-options-popup',
    template: ''
})
export class OptionsPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private optionsPopupService: OptionsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.optionsPopupService
                    .open(OptionsDialogComponent as Component, params['id']);
            } else {
                this.optionsPopupService
                    .open(OptionsDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
