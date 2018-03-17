/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { SampleApp1TestModule } from '../../../test.module';
import { OptionsComponent } from '../../../../../../main/webapp/app/entities/options/options.component';
import { OptionsService } from '../../../../../../main/webapp/app/entities/options/options.service';
import { Options } from '../../../../../../main/webapp/app/entities/options/options.model';

describe('Component Tests', () => {

    describe('Options Management Component', () => {
        let comp: OptionsComponent;
        let fixture: ComponentFixture<OptionsComponent>;
        let service: OptionsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SampleApp1TestModule],
                declarations: [OptionsComponent],
                providers: [
                    OptionsService
                ]
            })
            .overrideTemplate(OptionsComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(OptionsComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(OptionsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Options(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.options[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
