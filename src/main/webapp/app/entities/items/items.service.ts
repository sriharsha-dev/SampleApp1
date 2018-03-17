import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Items } from './items.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Items>;

@Injectable()
export class ItemsService {

    private resourceUrl =  SERVER_API_URL + 'api/items';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    create(items: Items): Observable<EntityResponseType> {
        const copy = this.convert(items);
        return this.http.post<Items>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(items: Items): Observable<EntityResponseType> {
        const copy = this.convert(items);
        return this.http.put<Items>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Items>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Items[]>> {
        const options = createRequestOption(req);
        return this.http.get<Items[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Items[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Items = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Items[]>): HttpResponse<Items[]> {
        const jsonResponse: Items[] = res.body;
        const body: Items[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Items.
     */
    private convertItemFromServer(items: Items): Items {
        const copy: Items = Object.assign({}, items);
        copy.createdOn = this.dateUtils
            .convertDateTimeFromServer(items.createdOn);
        return copy;
    }

    /**
     * Convert a Items to a JSON which can be sent to the server.
     */
    private convert(items: Items): Items {
        const copy: Items = Object.assign({}, items);

        copy.createdOn = this.dateUtils.toDate(items.createdOn);
        return copy;
    }
}
