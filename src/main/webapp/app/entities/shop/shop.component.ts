import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Shop } from './shop.model';
import { ShopService } from './shop.service';
import { Principal } from '../../shared';

@Component({
    selector: 'jhi-shop',
    templateUrl: './shop.component.html'
})
export class ShopComponent implements OnInit, OnDestroy {
shops: Shop[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private shopService: ShopService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.shopService.query().subscribe(
            (res: HttpResponse<Shop[]>) => {
                this.shops = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInShops();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Shop) {
        return item.id;
    }
    registerChangeInShops() {
        this.eventSubscriber = this.eventManager.subscribe('shopListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
