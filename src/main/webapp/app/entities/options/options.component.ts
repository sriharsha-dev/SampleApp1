import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Options } from './options.model';
import { OptionsService } from './options.service';
import { Principal } from '../../shared';

@Component({
    selector: 'jhi-options',
    templateUrl: './options.component.html'
})
export class OptionsComponent implements OnInit, OnDestroy {
options: Options[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private optionsService: OptionsService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.optionsService.query().subscribe(
            (res: HttpResponse<Options[]>) => {
                this.options = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInOptions();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Options) {
        return item.id;
    }
    registerChangeInOptions() {
        this.eventSubscriber = this.eventManager.subscribe('optionsListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
