import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Parent } from 'app/shared/model/parent.model';
import { ParentService } from './parent.service';
import { ParentComponent } from './parent.component';
import { ParentDetailComponent } from './parent-detail.component';
import { ParentUpdateComponent } from './parent-update.component';
import { ParentDeletePopupComponent } from './parent-delete-dialog.component';
import { IParent } from 'app/shared/model/parent.model';

@Injectable({ providedIn: 'root' })
export class ParentResolve implements Resolve<IParent> {
  constructor(private service: ParentService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IParent> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Parent>) => response.ok),
        map((parent: HttpResponse<Parent>) => parent.body)
      );
    }
    return of(new Parent());
  }
}

export const parentRoute: Routes = [
  {
    path: '',
    component: ParentComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterApp.parent.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ParentDetailComponent,
    resolve: {
      parent: ParentResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterApp.parent.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ParentUpdateComponent,
    resolve: {
      parent: ParentResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterApp.parent.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ParentUpdateComponent,
    resolve: {
      parent: ParentResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterApp.parent.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const parentPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: ParentDeletePopupComponent,
    resolve: {
      parent: ParentResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterApp.parent.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
