import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../Services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit {
  email: string = '';
  password: string = '';
  errorMessage: string = '';

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit() {
    const currentUser = JSON.parse(localStorage.getItem('currentUser') || '{}');
    console.log('currentUser:', currentUser);
    if (Object.keys(currentUser).length !== 0) {
      this.router.navigate(['/home']);
    }
  }

  onSubmit() {
    console.log('Email:', this.email);
    console.log('Password:', this.password);

    // Appel du service pour l'authentification
    this.authService.login(this.email, this.password).subscribe(
      (response) => {
        console.log('Login successful:', response);
        if (response) {
          console.log('Login successful:', response);
          localStorage.setItem('currentUser', JSON.stringify(response));
          this.router.navigate(['/home']); // Redirection vers la page home
        } else {
          this.errorMessage = 'Email or password is incorrect';
        }
      },
      (error) => {
        console.error('Login error:', error);
        this.errorMessage = 'Email or password is incorrect';
      }
    );
  }
}
