package com.nsb.restaurant.listener;

import com.nsb.restaurant.model.FoodModel;

public interface FoodListener {
    void onClickFood(FoodModel foodModel);
    void onRemove(FoodModel foodModel);
}
