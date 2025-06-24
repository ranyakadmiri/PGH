import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthServiceService } from 'src/app/services/auth-service.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  errorMessage: string | null = null;
  isLoading = false;
  welcomeMessage = "Bienvenue chez Poulina - Connectez-vous pour continuer";
  companyTips = [
    "Explorez les dernières opportunités de carrière chez Poulina.",
    "Collaborez avec une équipe d'experts passionnés.",
    "Faites partie d'un groupe en constante évolution.",
    "Votre avenir commence ici, rejoignez-nous."
  ];

  constructor(
    private fb: FormBuilder,
    private authService: AuthServiceService,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.showWelcomeMessage();
  }

  showWelcomeMessage(): void {
    this.welcomeMessage = this.companyTips[Math.floor(Math.random() * this.companyTips.length)];
  }

  dismissError(): void {
    this.errorMessage = null;
  }

  onSubmit(): void {
    if (this.loginForm.invalid) {
      return;
    }

    this.isLoading = true;

    const credentials = this.loginForm.value;
    this.authService.login(credentials).subscribe({
      next: (response) => {
        this.authService.setToken(response.token);
        this.router.navigate(['/']); // Redirige après connexion réussie
      },
      error: (error) => {
        this.errorMessage = "Adresse e-mail ou mot de passe invalide.";
        this.isLoading = false;
      }
    });
  }
}
