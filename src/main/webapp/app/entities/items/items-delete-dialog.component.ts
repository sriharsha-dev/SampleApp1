import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Items } from './items.model';
import { ItemsPopupService } from './items-popup.service';
import { ItemsService } from './items.service';

@Component({
    selector: 'jhi-items-delete-dialog',
    templateUrl: './items-delete-dialog.component.html'
})
export class ItemsDeleteDialogComponent {

    items: Items;

    constructor(
        private itemsService: ItemsService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.itemsService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'itemsListModification',
                content: 'Deleted an items'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-items-delete-popup',
    template: ''
})
export class ItemsDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private itemsPopupService: ItemsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.itemsPopupService
                .open(ItemsDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
