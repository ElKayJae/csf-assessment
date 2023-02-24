import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Comment, Restaurant } from '../models';
import { RestaurantService } from '../restaurant-service';
import { Location } from '@angular/common'

@Component({
  selector: 'app-restaurant-details',
  templateUrl: './restaurant-details.component.html',
  styleUrls: ['./restaurant-details.component.css']
})
export class RestaurantDetailsComponent implements OnInit{
	
  id! : string
  restaurant! : Restaurant
  form! :FormGroup


	// TODO Task 4 and Task 5
	// For View 3
  constructor(private activatedRoute: ActivatedRoute, private restaurantService: RestaurantService,
     private fb: FormBuilder, private router: Router, private location: Location){}

  ngOnInit(): void {
    this.id = this.activatedRoute.snapshot.params['id']
    console.log(this.id)
    this.restaurantService.getRestaurant(this.id).then(v => {
      console.log(v)
      this.restaurant = v
    })
    this.form = this.createForm()
  }

  createForm(){
    return this.fb.group({
      name: this.fb.control<string>('', [Validators.required, Validators.minLength(3)]),
      rating: this.fb.control<number>(1,[Validators.min(1),Validators.max(5)]),
      text: this.fb.control('',[Validators.required])
    })
  }

  process(){
    console.log(this.form.value['text'])
    const comment: Comment = {
      restaurantId: this.id,
      name: this.form.value['name'],
      rating: this.form.value['rating'],
      text: this.form.value['text'],
    }
    this.restaurantService.postComment(comment)
    .then(v => console.log(v))
    .then(()=>this.router.navigate(['/']))
    .catch(error => console.error(error))
  }

  back(){
    this.location.back()
  }

  

}
