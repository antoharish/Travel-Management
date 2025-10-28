import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { AppComponent } from './app/app.component';

import { importProvidersFrom } from '@angular/core';
import { provideHttpClient } from '@angular/common/http';
import { provideRouter } from '@angular/router';
import { routes } from './app/app.routes';
import { HomePageComponent } from './app/home-page/home-page.component';
bootstrapApplication(AppComponent, appConfig)
  .catch((err) => console.error(err));

  bootstrapApplication(HomePageComponent);
  bootstrapApplication(AppComponent, {
    providers: [
      provideRouter(routes),
      provideHttpClient()
      
    ]
  }).catch((err) => console.error(err));