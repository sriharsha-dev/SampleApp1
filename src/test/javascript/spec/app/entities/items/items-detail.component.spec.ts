/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { SampleApp1TestModule } from '../../../test.module';
import { ItemsDetailComponent } from '../../../../../../main/webapp/app/entities/items/items-detail.component';
import { ItemsService } from '../../../../../../main/webapp/app/entities/items/items.service';
import { Items } from '../../../../../../main/webapp/app/entities/items/items.model';

describe('Component Tests', () => {

    describe('Items Management Detail Component', () => {
        let comp: ItemsDetailComponent;
        let fixture: ComponentFixture<ItemsDetailComponent>;
        let service: ItemsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SampleApp1TestModule],
                declarations: [ItemsDetailComponent],
                providers: [
                    ItemsService
                ]
            })
            .overrideTemplate(ItemsDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ItemsDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ItemsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Items(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.items).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
