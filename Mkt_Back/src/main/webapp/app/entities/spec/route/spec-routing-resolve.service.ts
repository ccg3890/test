import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISpec } from '../spec.model';
import { SpecService } from '../service/spec.service';

@Injectable({ providedIn: 'root' })
export class SpecRoutingResolveService implements Resolve<ISpec | null> {
  constructor(protected service: SpecService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISpec | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((spec: HttpResponse<ISpec>) => {
          if (spec.body) {
            return of(spec.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
