import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { FilterCsvComponent } from './filter-csv/filter-csv.component';

const routes: Routes = [
  { path: '', component: FilterCsvComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
