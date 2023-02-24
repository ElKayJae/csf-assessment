package vttp2022.csf.assessment.server.utilities;

import java.util.List;

import org.bson.Document;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import vttp2022.csf.assessment.server.models.Comment;
import vttp2022.csf.assessment.server.models.LatLng;
import vttp2022.csf.assessment.server.models.Restaurant;

public class Utils {

    public static Restaurant createRestaurant(Document d){
        
		Restaurant restaurant = new Restaurant();
		restaurant.setName(d.getString("name"));
		restaurant.setAddress(d.getString("address"));
		restaurant.setRestaurantId(d.getString("restaurant_id"));
		restaurant.setCuisine(d.getString("cuisine"));
		LatLng coords = new LatLng();
		List<Number> coordList = d.getList("coordinates", Number.class);
		coords.setLatitude(coordList.get(1).longValue());
		coords.setLongitude(coordList.get(0).longValue());
		restaurant.setCoordinates(coords);

		return restaurant;
		
    }

	public static JsonObject toJson(Restaurant r){
		LatLng coords = r.getCoordinates();
		JsonArray arr = Json.createArrayBuilder().add(coords.getLatitude()).add(coords.getLongitude()).build();
		return Json.createObjectBuilder().add("restaurantId", r.getRestaurantId())
										.add("name", r.getName())
										.add("cuisine",r.getCuisine())
										.add("address", r.getAddress())
										.add("coordinates", arr).build();

	}
	
	public static Comment createComment(JsonObject o){
		Comment c = new Comment();
		c.setName(o.getString("name"));
		c.setRating(o.getInt("rating"));
		c.setText(o.getString("text"));
		c.setRestaurantId(o.getString("restaurantId"));
		
		return c;
	}

	public static Document commentTDocument(Comment c){
		Document d = new Document();
		d.put("restaurant_id", c.getRestaurantId());
		d.put("name", c.getName());
		d.put("rating", c.getRating());
		d.put("text", c.getText());

		return d;
	}
}
