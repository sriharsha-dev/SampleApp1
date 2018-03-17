/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { SampleApp1TestModule } from '../../../test.module';
import { ItemsDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/items/items-delete-dialog.component';
import { ItemsService } from '../../../../../../main/webapp/app/entities/items/items.service';

describe('Component Tests', () => {

    describe('Items Management Delete Component', () => {
        let comp: ItemsDeleteDialogComponent;
        let fixture: ComponentFixture<ItemsDeleteDialogComponent>;
        let service: ItemsService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SampleApp1TestModule],
                declarations: [ItemsDeleteDialogComponent],
                providers: [
                    ItemsService
                ]
            })
            .overrideTemplate(ItemsDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ItemsDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ItemsService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(Observable.of({}));

                        // WHEN
                        comp.confirmDelete(123);
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith(123);
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
