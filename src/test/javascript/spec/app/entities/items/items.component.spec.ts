/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { SampleApp1TestModule } from '../../../test.module';
import { ItemsComponent } from '../../../../../../main/webapp/app/entities/items/items.component';
import { ItemsService } from '../../../../../../main/webapp/app/entities/items/items.service';
import { Items } from '../../../../../../main/webapp/app/entities/items/items.model';

describe('Component Tests', () => {

    describe('Items Management Component', () => {
        let comp: ItemsComponent;
        let fixture: ComponentFixture<ItemsComponent>;
        let service: ItemsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SampleApp1TestModule],
                declarations: [ItemsComponent],
                providers: [
                    ItemsService
                ]
            })
            .overrideTemplate(ItemsComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ItemsComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ItemsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Items(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.items[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
