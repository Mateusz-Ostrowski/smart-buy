import {Route, Routes} from '@angular/router';

import { HomeComponent } from './home.component';

export const homeRoutes: Routes = [
  {
    path: '',
    component: HomeComponent,
    data: {
      pageTitle: 'Welcome, Java Hipster!',
    }
  },
  {
    path: 'categories/:id',
    component: HomeComponent,
    data: {
      pageTitle: 'Welcome, Java Hipster!',
    }
  },

];
