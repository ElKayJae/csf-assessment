import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import {HttpClientModule} from '@angular/common/http';

import { AppComponent } from './app.component';
import { CuisineListComponent } from './components/cuisine-list.component';
import { RestaurantCuisineComponent } from './components/restaurant-cuisine.component';
import { RestaurantDetailsComponent } from './components/restaurant-details.component';
import { RestaurantService } from './restaurant-service';
import { RouterModule, Routes } from '@angular/router';

const appRoutes: Routes =[
  { path: '', component :CuisineListComponent},
  { path: 'cuisine/:cuisine', component :RestaurantCuisineComponent},
  { path: 'restaurant/:id', component :RestaurantDetailsComponent},
  { path: '*', redirectTo: '/', pathMatch: 'full'},
  // '*' have to be last
]

@NgModule({
  declarations: [
    AppComponent,
    CuisineListComponent,
    RestaurantCuisineComponent,
    RestaurantDetailsComponent
  ],
  imports: [
    BrowserModule,
    ReactiveFormsModule,
    HttpClientModule,
    RouterModule.forRoot(appRoutes, {useHash: true})
  ],
  providers: [RestaurantService],
  bootstrap: [AppComponent]
})
export class AppModule { }
