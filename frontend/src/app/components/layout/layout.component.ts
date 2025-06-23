
import { Component, OnInit } from '@angular/core';
import { AuthServiceService } from 'src/app/services/auth-service.service';
import WOW from 'wowjs';

@Component({
  selector: 'app-layout',
  templateUrl: './layout.component.html',
  styleUrls: ['./layout.component.css']
})
export class LayoutComponent  implements OnInit {
    title = 'frontend-PGH';
      userEmail: string | null = null;
 email: string | null = null;
        constructor(public authService: AuthServiceService) {}
  ngOnInit() {
 if ('MutationObserver' in window) {
    const wow = new WOW.WOW({
      boxClass: 'wow', // Default class used for animations
      animateClass: 'animated', // Class used to trigger animations
      offset: 0, // Distance to trigger animations (default 0)
      mobile: true, // Enable animations on mobile devices
      live: true, // Check for new DOM elements
    });
    wow.init();
  } else {
    console.warn('WOW.js animations disabled due to unsupported features.');
  }


 this.authService.email$.subscribe(email => {
    console.log('Email reçu dans le composant:', email);
    this.email = email;
  });

  if (!this.email) {
    this.email = this.authService.getEmail();
    console.log('Email récupéré localStorage:', this.email);
  }
}
 logout() {
    this.authService.logout();
  }
}

    
