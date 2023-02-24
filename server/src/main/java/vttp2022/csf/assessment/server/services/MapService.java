package vttp2022.csf.assessment.server.services;

import java.util.Collections;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import vttp2022.csf.assessment.server.models.LatLng;
import vttp2022.csf.assessment.server.models.Restaurant;

@Service
public class MapService {
    private static final String URL = "http://map.chuklee.com";

    public static byte[] getMap(Restaurant r){

        LatLng coords = r.getCoordinates();
        String requestUrl = UriComponentsBuilder.fromUriString(URL)
                            .queryParam("lat", coords.getLatitude())
                            .queryParam("lng", coords.getLongitude())
                            .toUriString();
                            
        System.out.println(requestUrl);

        RestTemplate template = new RestTemplate();
        ResponseEntity<byte[]> resp = null;

        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.IMAGE_PNG));
            HttpEntity<String> entity = new HttpEntity<>(headers);
            resp = template.exchange(requestUrl, HttpMethod.GET, entity, byte[].class);
            System.out.println(resp.getBody());

        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return resp.getBody();


    }
}
