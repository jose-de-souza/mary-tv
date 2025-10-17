import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HeaderComponent } from './components/header/header'; // Correct path

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, HeaderComponent],
  templateUrl: './app.html', // Corrected path
  styleUrls: ['./app.scss'],   // Corrected path
})
export class AppComponent {
  title = 'mary-tv'; // Title is a property, not a function
}