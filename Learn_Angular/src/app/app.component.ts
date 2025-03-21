import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {FooterComponent} from './share/footer/footer.component';
import { HeaderComponent } from "./user/header/header.component";
import { HomeComponent } from "./user/home/home.component";

@Component({
  selector: 'app-root',
  standalone: true, // Thêm dòng này để đánh dấu là standalone
  imports: [RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'DioShop_Frontend';
}
