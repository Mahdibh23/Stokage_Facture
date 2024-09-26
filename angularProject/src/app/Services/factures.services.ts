// src/app/Services/facture.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Facture } from '../models/facture';

@Injectable({
  providedIn: 'root',
})
export class FactureService {
  private apiUrl = 'http://localhost:8080/api/factures';

  constructor(private http: HttpClient) {}

  getFacturesByEntrepriseId(entrepriseId: number): Observable<Facture[]> {
    return this.http.get<Facture[]>(
      `${this.apiUrl}/entreprises/${entrepriseId}`
    );
  }
}
