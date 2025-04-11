import { createFeatureSelector, createSelector } from '@ngrx/store';
import { SalesState } from './sales.reducer';
export const selectSalesState = createFeatureSelector<SalesState>('sales');
export const selectAllSales = createSelector(selectSalesState, state => state.sales);