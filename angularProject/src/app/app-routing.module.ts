// src/app/app-routing.module.ts
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { FactureComponent } from './facture/facture.component';
import { DisabledEntreprisesComponent } from './disabled-entreprises/disabled-entreprises.component';
const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'home', component: HomeComponent },
  { path: 'entreprise/:id/factures', component: FactureComponent },
  { path: 'disabled-entreprises', component: DisabledEntreprisesComponent },
  { path: '', redirectTo: '/login', pathMatch: 'full' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
