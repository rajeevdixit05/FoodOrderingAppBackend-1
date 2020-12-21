package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.ItemList;
import com.upgrad.FoodOrderingApp.api.model.ItemListResponse;
import com.upgrad.FoodOrderingApp.service.business.ItemService;
import com.upgrad.FoodOrderingApp.service.business.RestaurantService;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class ItemController {

    @Autowired private ItemService itemService;

    @Autowired private RestaurantService restaurantService;

    /**
     * This method takes restaurant_id from customer, returns top 5 popular items
     *
     * @param restaurantId restaurant id as request path var
     * @return ResponseEntity with list of items
     * @throws RestaurantNotFoundException on invalid restaurantId
     */
    @CrossOrigin
    @RequestMapping(
            method = RequestMethod.GET,
            path = "/item/restaurant/{restaurant_id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemListResponse> getItemsByPopularity(
            @PathVariable("restaurant_id") final String restaurantId) throws RestaurantNotFoundException {

        // Retrieve RestaurantEntity matching restaurantId from database
        RestaurantEntity restaurantEntity = restaurantService.restaurantByUUID(restaurantId);

        // Retrieve ItemEntity List matching restaurantId and sorted by popularity
        List<ItemEntity> itemList = itemService.getItemsByPopularity(restaurantEntity);

        ItemListResponse itemListResponse = new ItemListResponse();

        // Map retrieved ItemEntity to Response Object List
        int count = 0;
        for (ItemEntity itemEntity : itemList) {
            if (count < 5) {
                ItemList items =
                        new ItemList()
                                .id(UUID.fromString(itemEntity.getUuid()))
                                .itemName(itemEntity.getItemName())
                                .price(itemEntity.getPrice())
                                .itemType(ItemList.ItemTypeEnum.fromValue(itemEntity.getType().getValue()));
                itemListResponse.add(items);
                count = count + 1;
            } else {
                break;
            }
        }

        return new ResponseEntity<ItemListResponse>(itemListResponse, HttpStatus.OK);
    }
}
