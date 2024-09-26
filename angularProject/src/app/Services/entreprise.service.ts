import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Facture } from '../models/facture';
import { Entreprise } from '../models/entreprise';

@Injectable({
  providedIn: 'root',
})
export class EntrepriseService {
  private baseUrl = 'http://localhost:8080/api/entreprises'; // Remplacez par l'URL de votre backend

  constructor(private http: HttpClient) {}

  getAllEntreprises(): Observable<any> {
    return this.http.get(`${this.baseUrl}/`);
  }

  getEntrepriseById(id: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/${id}`);
  }

  createEntreprise(entreprise: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/`, entreprise);
  }

  updateEntreprise(id: number, entreprise: any): Observable<any> {
    return this.http.put(`${this.baseUrl}/${id}`, entreprise);
  }
  desactiverEntreprise(id: number): Observable<void> {
    return this.http.put<void>(`${this.baseUrl}/${id}/desactiver`, null);
  }

  getEntreprisesDesactivees(): Observable<Entreprise[]> {
    return this.http.get<Entreprise[]>(`${this.baseUrl}/desactivees`);
  }
  deleteEntreprise(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${id}`);
  }
}
