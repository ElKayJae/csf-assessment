package vttp2022.csf.assessment.server.repositories;

import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import vttp2022.csf.assessment.server.models.Comment;
import vttp2022.csf.assessment.server.models.Restaurant;
import vttp2022.csf.assessment.server.utilities.Utils;

@Repository
public class RestaurantRepository {

	private final String C_RESTAURANT = "restaurants";
	private final String C_COMMENT = "comments";

	@Autowired
	MongoTemplate template;
	// TODO Task 2
	// Use this method to retrive a list of cuisines from the restaurant collection
	// You can add any parameters (if any) and the return type 
	// DO NOT CHNAGE THE METHOD'S NAME
	// Write the Mongo native query above for this method
	//  db.restaurants.distinct("cuisine")
	public Optional<JsonArray> getCuisines() {
		// Implmementation in here
		List<String> cuisineList = template.findDistinct(new Query(), "cuisine", C_RESTAURANT, String.class);

		if (cuisineList.size() == 0) return Optional.empty();

		JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
		cuisineList.stream().forEach(c -> {
			c = c.replace("/", "_");
			arrayBuilder.add(c);
		});

		return Optional.of(arrayBuilder.build());
	}

	// TODO Task 3
	// Use this method to retrive a all restaurants for a particular cuisine
	// You can add any parameters (if any) and the return type 
	// DO NOT CHNAGE THE METHOD'S NAME
	// Write the Mongo native query above for this method
	 
	// db.restaurants.find({ cuisine: "African"}, {name:1, restaurant_id:1, _id:0}).sort({name:1})
	public Optional<JsonArray> getRestaurantsByCuisine(String cuisine) {
		// Implmementation in here
		Criteria c = Criteria.where("cuisine").is(cuisine);
		Query q = Query.query(c).with(Sort.by(Direction.ASC, "name"));
		q.fields().include("name","restaurant_id").exclude("_id");
		List<Document> docList = template.find(q, Document.class, C_RESTAURANT);

		if (docList.size() == 0) return Optional.empty();

		JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
		docList.stream().forEach(d -> {
			arrayBuilder.add(
			Json.createObjectBuilder().add("name", d.getString("name"))
			.add("restaurantId", d.getString("restaurant_id"))
			);
		});

		return Optional.of(arrayBuilder.build());
	}

	// TODO Task 4
	// Use this method to find a specific restaurant
	// You can add any parameters (if any) 
	// DO NOT CHNAGE THE METHOD'S NAME OR THE RETURN TYPE
	// Write the Mongo native query above for this method
	//  db.restaurants.aggregate([
	// {$match: {_id: ObjectId("63f82b85d12aa8924c6f145a")}},
	// {$project: 
	// 	{
	// 		_id:0,
	// 		restaurant_id: "$restaurant_id", 
	// 		name:"$name",
	// 		cuisine:"$cuisine",
	// 		address: {$concat:["$address.building",", ","$address.street",", ","$address.zipcode",", ","$borough"]},
	// 		coordinates: "$address.coord"
	// 		}
	// }
	// ])
	public Optional<Restaurant> getRestaurant(String id) {
		// Implmementation in here
		Criteria c = Criteria.where("restaurant_id").is(id);
		MatchOperation matchOperation = Aggregation.match(c);
		ProjectionOperation projectOperation = Aggregation.project("restaurant_id","name","cuisine")
			.andExpression("concat(address.building,', ',address.street,', ',address.zipcode,', ',borough)").as("address")
			.and("$address.coord").as("coordinates");
		Aggregation pipline = Aggregation.newAggregation(matchOperation,projectOperation);
		AggregationResults<Document> results = template.aggregate(pipline, C_RESTAURANT , Document.class);

		if( results.getMappedResults().size() == 0) return Optional.empty();

		Document d = results.getMappedResults().get(0);

		return Optional.of(Utils.createRestaurant(d));
		
	}

	// TODO Task 5
	// Use this method to insert a comment into the restaurant database
	// DO NOT CHNAGE THE METHOD'S NAME OR THE RETURN TYPE
	// Write the Mongo native query above for this method
	//  db.comments.insert(
		// {restaurant_id : 123425 },
		// {name : xxx},
		// {rating : 1},
		// {text : lorem ipsum}
		// )
	public void addComment(Comment comment) {
		// Implmementation in here
		Document toInsert = Utils.commentTDocument(comment);
		template.insert(toInsert, C_COMMENT);
	}
	
	// You may add other methods to this class

}
