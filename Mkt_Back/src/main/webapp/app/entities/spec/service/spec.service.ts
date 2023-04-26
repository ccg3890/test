import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISpec, NewSpec } from '../spec.model';

export type PartialUpdateSpec = Partial<ISpec> & Pick<ISpec, 'id'>;

type RestOf<T extends ISpec | NewSpec> = Omit<T, 'createDt' | 'modifyDt'> & {
  createDt?: string | null;
  modifyDt?: string | null;
};

export type RestSpec = RestOf<ISpec>;

export type NewRestSpec = RestOf<NewSpec>;

export type PartialUpdateRestSpec = RestOf<PartialUpdateSpec>;

export type EntityResponseType = HttpResponse<ISpec>;
export type EntityArrayResponseType = HttpResponse<ISpec[]>;

@Injectable({ providedIn: 'root' })
export class SpecService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/specs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(spec: NewSpec): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(spec);
    return this.http.post<RestSpec>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(spec: ISpec): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(spec);
    return this.http
      .put<RestSpec>(`${this.resourceUrl}/${this.getSpecIdentifier(spec)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(spec: PartialUpdateSpec): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(spec);
    return this.http
      .patch<RestSpec>(`${this.resourceUrl}/${this.getSpecIdentifier(spec)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http
      .get<RestSpec>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestSpec[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSpecIdentifier(spec: Pick<ISpec, 'id'>): string {
    return spec.id;
  }

  compareSpec(o1: Pick<ISpec, 'id'> | null, o2: Pick<ISpec, 'id'> | null): boolean {
    return o1 && o2 ? this.getSpecIdentifier(o1) === this.getSpecIdentifier(o2) : o1 === o2;
  }

  addSpecToCollectionIfMissing<Type extends Pick<ISpec, 'id'>>(
    specCollection: Type[],
    ...specsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const specs: Type[] = specsToCheck.filter(isPresent);
    if (specs.length > 0) {
      const specCollectionIdentifiers = specCollection.map(specItem => this.getSpecIdentifier(specItem)!);
      const specsToAdd = specs.filter(specItem => {
        const specIdentifier = this.getSpecIdentifier(specItem);
        if (specCollectionIdentifiers.includes(specIdentifier)) {
          return false;
        }
        specCollectionIdentifiers.push(specIdentifier);
        return true;
      });
      return [...specsToAdd, ...specCollection];
    }
    return specCollection;
  }

  protected convertDateFromClient<T extends ISpec | NewSpec | PartialUpdateSpec>(spec: T): RestOf<T> {
    return {
      ...spec,
      createDt: spec.createDt?.format(DATE_FORMAT) ?? null,
      modifyDt: spec.modifyDt?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restSpec: RestSpec): ISpec {
    return {
      ...restSpec,
      createDt: restSpec.createDt ? dayjs(restSpec.createDt) : undefined,
      modifyDt: restSpec.modifyDt ? dayjs(restSpec.modifyDt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestSpec>): HttpResponse<ISpec> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestSpec[]>): HttpResponse<ISpec[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
