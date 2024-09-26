import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { MatDialogModule } from '@angular/material/dialog';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { HomeComponent } from './home/home.component';
import { AppRoutingModule } from './app-routing.module';
import { FormulaireComponent } from './formulaire/formulaire.component';
import { FactureComponent } from './facture/facture.component';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { ReactiveFormsModule } from '@angular/forms';
import { ModalFactureComponent } from './modal-facture/modal-facture.component';
import { DisabledEntreprisesComponent } from './disabled-entreprises/disabled-entreprises.component';
import { ModalRecuComponent } from './modal-recu/modal-recu.component';
import { SafePipe } from './safe.pipe';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    HomeComponent,
    FormulaireComponent,
    FactureComponent,
    DisabledEntreprisesComponent,
    ModalFactureComponent,
    ModalRecuComponent,
    SafePipe,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    CommonModule,
    MatDialogModule,
    BrowserAnimationsModule,
    ReactiveFormsModule,
  ],
  providers: [provideAnimationsAsync()],
  bootstrap: [AppComponent],
})
export class AppModule {}
