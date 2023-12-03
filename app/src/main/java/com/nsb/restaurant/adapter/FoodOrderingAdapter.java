package com.nsb.restaurant.adapter;

import static com.nsb.restaurant.util.Constant.formatSalary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nsb.restaurant.activity.user.OrderFoodActivity;
import com.nsb.restaurant.databinding.ItemFoodOrderingBinding;
import com.nsb.restaurant.listener.FoodListener;
import com.nsb.restaurant.model.FoodModel;
import com.nsb.restaurant.util.OnSwipeTouchListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FoodOrderingAdapter extends RecyclerView.Adapter<FoodOrderingAdapter.FoodOrderingHolder> {
    private final List<FoodModel> foodModelList;
    public final Context context;
    private static final int SWIPE_THRESHOLD = 100;
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;
    private final FoodListener listener;


    public FoodOrderingAdapter(List<FoodModel> foodModelList, Context context, FoodListener listener) {
        this.foodModelList = foodModelList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FoodOrderingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFoodOrderingBinding itemLayoutFoodBinding = ItemFoodOrderingBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new FoodOrderingHolder(itemLayoutFoodBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodOrderingHolder holder, int position) {
        holder.setData(foodModelList.get(position));
    }

    @Override
    public int getItemCount() {
        return foodModelList.size();
    }

    class FoodOrderingHolder extends RecyclerView.ViewHolder {

        ItemFoodOrderingBinding binding;

        FoodOrderingHolder(ItemFoodOrderingBinding itemFoodOrderingBinding) {
            super(itemFoodOrderingBinding.getRoot());
            binding = itemFoodOrderingBinding;
        }

        void setData(FoodModel foodModel) {
            binding.textNameFood.setText(foodModel.getName());
            binding.textPrice.setText(formatSalary(foodModel.getPrice()) + " vnd");
            if (foodModel.getImage() != null && !foodModel.getImage().equals("")) {
                Picasso.get().load(foodModel.getImage()).into(binding.imageFood);
            }
            binding.layoutFood.setOnTouchListener(new OnSwipeTouchListener(context, binding.getRoot()) {
                @Override
                public void onSwipeLeft() {
                    super.onSwipeLeft();
                    binding.imageDelete.setVisibility(View.VISIBLE);
                    binding.textNameFood.setMaxLines(1);
                }

                @Override
                public void onSwipeRight() {
                    super.onSwipeRight();
                    binding.imageDelete.setVisibility(View.GONE);
                    binding.textNameFood.setMaxLines(2);
                }
            });

            binding.textAmount.setText(String.valueOf(foodModel.getNumOfFood()));

            binding.imageDelete.setOnClickListener(v -> {
                listener.onRemove(foodModel);
                binding.imageDelete.setVisibility(View.GONE);
            });

            binding.imagePlus.setOnClickListener(v -> {
                int amount = foodModel.getNumOfFood();
                amount++;
                binding.textAmount.setText(String.valueOf(amount));
                foodModel.setNumOfFood(amount);

                //Đặt thêm món----------------------------------------------------------------------

//                for (int i = 0; i < OrderFoodActivity.listFoodFirst.size(); i++) {
//                    if (OrderFoodActivity.listFoodFirst.get(i).getId().equals(foodModel.getId()) &&
//                            amount <= OrderFoodActivity.listFoodFirst.get(i).getNumOfFood()) {
//                        if (OrderFoodActivity.isAddFood == true) {
//                            OrderFoodActivity.setTotalMoney(OrderFoodActivity.listFoodBooked);
//                        } else {
//                            OrderFoodActivity.setTotalMoney(OrderFoodActivity.foodModelListOrdering);
//                        }
//                        return;
//                    }
//                }
//                boolean isAdd = true;
//                for (int i = 0; i < OrderFoodActivity.listFoodBooked.size(); i++) {
//                    if (OrderFoodActivity.listFoodBooked.get(i).getId().equals(foodModel.getId())) {
//                        int num = OrderFoodActivity.listFoodBooked.get(i).getNumOfFood();
//                        OrderFoodActivity.listFoodBooked.get(i).setNumOfFood(num += 1);
//                        isAdd = false;
//                        break;
//                    }
//                }
//                if (isAdd) {
//                    OrderFoodActivity.listFoodBooked.add(new FoodModel(
//                            foodModel.getId(),
//                            foodModel.getName(),
//                            foodModel.getPrice(),
//                            foodModel.getImage(),
//                            1));
//                }
                //----------------------------------------------------------------------------------

//                if (OrderFoodActivity.isAddFood == true) {
//                    OrderFoodActivity.setTotalMoney(OrderFoodActivity.listFoodBooked);
//                } else {
//                    OrderFoodActivity.setTotalMoney(OrderFoodActivity.foodModelListOrdering);
//                }
               OrderFoodActivity.setTotalMoney(OrderFoodActivity.foodModelListOrdering);
            });

            binding.imageMinus.setOnClickListener(v -> {
                if (foodModel.getNumOfFood() > 0) {
                    //Đặt thêm món----------------------------------------------------------------------
//                    for (int i = 0; i < OrderFoodActivity.listFoodBooked.size(); i++) {
//                        if (OrderFoodActivity.listFoodBooked.get(i).getId().equals(foodModel.getId())) {
//                            int num = OrderFoodActivity.listFoodBooked.get(i).getNumOfFood();
//                            num -= 1;
//                            if (num == 0) {
//                                OrderFoodActivity.listFoodBooked.remove(i);
//                            } else {
//                                OrderFoodActivity.listFoodBooked.get(i).setNumOfFood(num);
//                            }
//                            break;
//                        }
//                    }
                    //----------------------------------------------------------------------------------

                    int amount = foodModel.getNumOfFood();
                    amount--;
//                    binding.textAmount.setText(String.valueOf(amount));
//                    foodModel.setNumOfFood(amount);

                    if (amount == 0) {
                        int position = -1;
                        for (int i = 0; i < foodModelList.size(); i++) {
                            if (foodModelList.get(i).getId().equals(foodModel.getId())) {
                                position = i;
                                foodModelList.remove(foodModel);
                                notifyItemRemoved(position);
                                break;
                            }
                        }
                    } else {
                        binding.textAmount.setText(String.valueOf(amount));
                        foodModel.setNumOfFood(amount);
                    }
                    OrderFoodActivity.setTotalMoney(OrderFoodActivity.foodModelListOrdering);
//                    if (OrderFoodActivity.isAddFood == true) {
//                        OrderFoodActivity.setTotalMoney(OrderFoodActivity.listFoodBooked);
//                    } else {
//                        OrderFoodActivity.setTotalMoney(OrderFoodActivity.foodModelListOrdering);
//                    }
                }
            });
        }
    }
}
