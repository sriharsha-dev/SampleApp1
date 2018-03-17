/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { SampleApp1TestModule } from '../../../test.module';
import { OptionsDetailComponent } from '../../../../../../main/webapp/app/entities/options/options-detail.component';
import { OptionsService } from '../../../../../../main/webapp/app/entities/options/options.service';
import { Options } from '../../../../../../main/webapp/app/entities/options/options.model';

describe('Component Tests', () => {

    describe('Options Management Detail Component', () => {
        let comp: OptionsDetailComponent;
        let fixture: ComponentFixture<OptionsDetailComponent>;
        let service: OptionsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SampleApp1TestModule],
                declarations: [OptionsDetailComponent],
                providers: [
                    OptionsService
                ]
            })
            .overrideTemplate(OptionsDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(OptionsDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(OptionsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Options(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.options).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
