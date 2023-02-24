package vttp2022.csf.assessment.server.repositories;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import vttp2022.csf.assessment.server.models.Restaurant;
import vttp2022.csf.assessment.server.services.MapService;

@Repository
public class MapCache {

	@Autowired
	private AmazonS3 s3Client;

	// TODO Task 4
	// Use this method to retrieve the map
	// You can add any parameters (if any) and the return type 
	// DO NOT CHNAGE THE METHOD'S NAME
	public String getMap(Restaurant r){
		// Implmementation in here
		Boolean exists = s3Client.doesObjectExist("vttpnus", "restaurantMap/%s".formatted(r.getRestaurantId()));
		if (exists) return "https://vttpnus.sgp1.digitaloceanspaces.com/restaurantMap/%s".formatted(r.getRestaurantId());
		String url = "";
		try {
			url = upload(r);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return url;

	}

	// You may add other methods to this class

	public String upload(Restaurant r) throws IOException{

		byte[] file = MapService.getMap(r);

        //user data
        Map<String, String> postData = new HashMap<>();
        postData.put("restuarantId", r.getRestaurantId());

        //metadata of the file
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setUserMetadata(postData);

        
        //create a put request
        PutObjectRequest putReq = new PutObjectRequest("vttpnus", "restaurantMap/%s".formatted(r.getRestaurantId()), new ByteArrayInputStream(file) ,metadata);
        
        //allow public access
        putReq.withCannedAcl(CannedAccessControlList.PublicRead);
        
        s3Client.putObject(putReq);

        String imageUrl = "https://vttpnus.sgp1.digitaloceanspaces.com/restaurantMap/%s".formatted(r.getRestaurantId());
        
        return imageUrl;

    }
}
