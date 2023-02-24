import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Restaurant } from '../models';
import { RestaurantService } from '../restaurant-service';

@Component({
  selector: 'app-restaurant-cuisine',
  templateUrl: './restaurant-cuisine.component.html',
  styleUrls: ['./restaurant-cuisine.component.css']
})
export class RestaurantCuisineComponent implements OnInit{
	
	// TODO Task 3
	// For View 2
  cuisine!: string
  restaurantList! : Restaurant[]

  constructor(private activatedRoute: ActivatedRoute, private restaurantService: RestaurantService){}

  ngOnInit(): void {
    this.cuisine = this.activatedRoute.snapshot.params['cuisine']
    console.log(this.cuisine)
    this.restaurantService.getRestaurantsByCuisine(this.cuisine).then( v => {
      console.log(v)
      this.restaurantList = v
      }).catch(error => console.error(error))

    
  }

}
