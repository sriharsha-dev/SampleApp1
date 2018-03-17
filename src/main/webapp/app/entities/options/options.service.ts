import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Options } from './options.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Options>;

@Injectable()
export class OptionsService {

    private resourceUrl =  SERVER_API_URL + 'api/options';

    constructor(private http: HttpClient) { }

    create(options: Options): Observable<EntityResponseType> {
        const copy = this.convert(options);
        return this.http.post<Options>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(options: Options): Observable<EntityResponseType> {
        const copy = this.convert(options);
        return this.http.put<Options>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Options>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Options[]>> {
        const options = createRequestOption(req);
        return this.http.get<Options[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Options[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Options = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Options[]>): HttpResponse<Options[]> {
        const jsonResponse: Options[] = res.body;
        const body: Options[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Options.
     */
    private convertItemFromServer(options: Options): Options {
        const copy: Options = Object.assign({}, options);
        return copy;
    }

    /**
     * Convert a Options to a JSON which can be sent to the server.
     */
    private convert(options: Options): Options {
        const copy: Options = Object.assign({}, options);
        return copy;
    }
}
