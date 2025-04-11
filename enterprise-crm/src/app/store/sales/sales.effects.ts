import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import * as SalesActions from './sales.actions';
import { switchMap, map } from 'rxjs/operators';
import { SalesService } from '../../pages/sales-automation/service/sales.service';
@Injectable()
export class SalesEffects {
 loadSales$ = createEffect(() =>
   this.actions$.pipe(
     ofType(SalesActions.loadSales),
     switchMap(() =>
       this.salesService.getSales().pipe(
         map(sales => SalesActions.loadSalesSuccess({ sales }))
       )
     )
   )
 );
 constructor(private actions$: Actions, private salesService: SalesService) {}
}