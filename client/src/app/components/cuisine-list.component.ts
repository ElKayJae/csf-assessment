import { Component, OnInit } from '@angular/core';
import { RestaurantService } from '../restaurant-service';

@Component({
  selector: 'app-cuisine-list',
  templateUrl: './cuisine-list.component.html',
  styleUrls: ['./cuisine-list.component.css']
})
export class CuisineListComponent implements OnInit{

  cuisineList! :string[]

	// TODO Task 2
	// For View 1

  constructor(private restaurantService: RestaurantService){}

  ngOnInit(): void {
    this.restaurantService.getCuisineList().then(
      v => {
        this.cuisineList = v
        console.log(this.cuisineList)
      }
    ).catch(error => console.error(error))
      
    
  }

}
