import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Items } from './items.model';
import { ItemsService } from './items.service';

@Component({
    selector: 'jhi-items-detail',
    templateUrl: './items-detail.component.html'
})
export class ItemsDetailComponent implements OnInit, OnDestroy {

    items: Items;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private itemsService: ItemsService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInItems();
    }

    load(id) {
        this.itemsService.find(id)
            .subscribe((itemsResponse: HttpResponse<Items>) => {
                this.items = itemsResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInItems() {
        this.eventSubscriber = this.eventManager.subscribe(
            'itemsListModification',
            (response) => this.load(this.items.id)
        );
    }
}
