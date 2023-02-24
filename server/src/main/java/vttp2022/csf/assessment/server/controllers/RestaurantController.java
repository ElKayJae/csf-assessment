package vttp2022.csf.assessment.server.controllers;

import java.io.StringReader;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp2022.csf.assessment.server.models.Comment;
import vttp2022.csf.assessment.server.models.Restaurant;
import vttp2022.csf.assessment.server.services.RestaurantService;
import vttp2022.csf.assessment.server.utilities.Utils;

@Controller
@RequestMapping(path = "/api")
public class RestaurantController {

    @Autowired
    RestaurantService restaurantService;
    
    @ResponseBody
    @GetMapping(path = "/cuisines")
    public ResponseEntity<String> getCuisines(){
        Optional<JsonArray> opt = restaurantService.getCuisines();
        if( opt.isEmpty()){
            JsonObject o = Json.createObjectBuilder().add("error", "not found").build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(o.toString());
        }
        return ResponseEntity.status(HttpStatus.OK).body(opt.get().toString());
    }

    @ResponseBody
    @GetMapping(path = "/{cuisine}/restaurants")
    public ResponseEntity<String> getRestaurantsByCuisine(@PathVariable(required = true) String cuisine ){
        cuisine = cuisine.replace("_", "/");
        Optional<JsonArray> opt = restaurantService.getRestaurantsByCuisine(cuisine);
        if( opt.isEmpty()){
            JsonObject o = Json.createObjectBuilder().add("error", "not found").build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(o.toString());
        }
        return ResponseEntity.status(HttpStatus.OK).body(opt.get().toString());
    }

    @ResponseBody
    @GetMapping(path = "/restaurant/{id}")
    public ResponseEntity<String> getRestaurant(@PathVariable(required = true) String id ){
        
        Optional<Restaurant> opt = restaurantService.getRestaurant(id);
        if( opt.isEmpty()){
            JsonObject o = Json.createObjectBuilder().add("error", "id not found").build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(o.toString());
        }
        JsonObject o = Utils.toJson(opt.get());
        System.out.println(">>>>>>> restaurant details sent");
        return ResponseEntity.status(HttpStatus.OK).body(o.toString());
    }

    @ResponseBody
    @PostMapping(path = "/comments")
    public ResponseEntity<String> postComment(@RequestBody String json ){

        JsonReader reader = Json.createReader(new StringReader(json));
        JsonObject o = reader.readObject();
        System.out.println("received >>>>>" + o.toString());
        Comment c = Utils.createComment(o);
        restaurantService.addComment(c);
        JsonObject message = Json.createObjectBuilder().add("message", "Comment posted").build();
        return ResponseEntity.status(HttpStatus.CREATED).body(message.toString());
    }
}
