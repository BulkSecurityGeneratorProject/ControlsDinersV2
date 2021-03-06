import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UsuarisProcesComponent } from './usuaris-proces.component';
import { UsuarisProcesDetailComponent } from './usuaris-proces-detail.component';

@Injectable()
export class UsuarisProcesResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const usuarisProcesRoute: Routes = [
    {
        path: 'usuaris-proces',
        component: UsuarisProcesComponent,
        resolve: {
            'pagingParams': UsuarisProcesResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'controlDinersApp.usuarisProces.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'usuaris-proces/:id',
        component: UsuarisProcesDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'controlDinersApp.usuarisProces.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];
