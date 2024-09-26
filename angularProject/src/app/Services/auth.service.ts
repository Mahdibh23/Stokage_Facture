import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api'; // Remplacez par votre URL API

  constructor(private http: HttpClient) {}

  login(email: string, password: string): Observable<any> {
    const endpoint = `${this.apiUrl}/users`;

    // Appel GET pour récupérer les utilisateurs
    return this.http.get<any[]>(endpoint).pipe(
      map((users) => {
        // Vérification si des utilisateurs ont été renvoyés
        if (users && users.length > 0) {
          const authenticatedUser = users.find(
            (user) => user.email === email && user.password === password
          );
          return authenticatedUser ? authenticatedUser : null;
        } else {
          return null; // Aucun utilisateur trouvé
        }
      })
    );
  }
}
