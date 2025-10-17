import { Routes } from '@angular/router';
import { AuthGuard } from './services/auth.guard';

export const appRoutes: Routes = [
  { path: '', redirectTo: 'items', pathMatch: 'full' },
  {
    path: 'login',
    loadComponent: () =>
      import('./components/login/login').then((m) => m.LoginComponent),
  },
  {
    path: 'items',
    loadComponent: () =>
      import('./components/items-list/items-list').then(
        (m) => m.ItemsListComponent
      ),
  },
  {
    path: 'headliners',
    loadComponent: () =>
      import('./components/headliners/headliners').then(
        (m) => m.HeadlinersComponent
      ),
  },
  {
    path: 'series/:seriesId/episodes',
    loadComponent: () =>
      import('./components/episodes-list/episodes-list').then( // Ensure this path is correct
        (m) => m.EpisodesListComponent
      ),
  },
  {
    path: 'user-maintenance',
    loadComponent: () =>
      import('./components/user-maintenance/user-maintenance').then(
        (m) => m.UserMaintenanceComponent
      ),
    canActivate: [AuthGuard],
  },
  { path: '**', redirectTo: 'items' },
];