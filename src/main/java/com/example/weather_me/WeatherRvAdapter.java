package com.example.weather_me;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather_me.R;
import com.example.weather_me.RVModel;
import com.squareup.picasso.Picasso;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WeatherRvAdapter extends RecyclerView.Adapter<WeatherRvAdapter.ViewHolder> {

    private Context context;
    private ArrayList<RVModel> weatherRvModelArrayList;

    public WeatherRvAdapter(Context context, ArrayList<RVModel> weatherRvModelArrayList) {
        this.context = context;
        this.weatherRvModelArrayList = weatherRvModelArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_rv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RVModel model = weatherRvModelArrayList.get(position);
        holder.temperatureTV.setText(model.getTemperature() + "°C");
        holder.conditionTV.setText(model.getCondition());
        holder.dayTV.setText(model.getDay());
        Picasso.get().load("https:" + model.getImage()).into(holder.imageIV);
        holder.windTV.setText(model.getWindSpeed() + " Km/h");

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm"); // Use HH for 24-hour format
        SimpleDateFormat outputFormat = new SimpleDateFormat("hh:mm a");

        try {
            Date date = inputFormat.parse(model.getDay());
            if (date != null) {
                holder.dayTV.setText(outputFormat.format(date));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return weatherRvModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView windTV, temperatureTV, dayTV,conditionTV;
        private ImageView imageIV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            windTV = itemView.findViewById(R.id.idTVWindSpeed);
            temperatureTV = itemView.findViewById(R.id.idTVTemperature);
            dayTV = itemView.findViewById(R.id.idTVDay);
            imageIV=itemView.findViewById(R.id.idIVImage);
            conditionTV = itemView.findViewById(R.id.idTVCondition);
        }
    }
}
