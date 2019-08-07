package adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import model.ResortHelper;
import www.andela.com.travelmantics.R;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.viewHolder>{

    private Context context;
    private List<ResortHelper> list;

    public MyAdapter(Context context, List<ResortHelper> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v;
        LayoutInflater inflater = LayoutInflater.from(context);
        v = inflater.inflate(R.layout.single_view, viewGroup, false);
        return new MyAdapter.viewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder viewHolder, int i) {
        ResortHelper helper = list.get(i);
        viewHolder.setImageView(helper.getImage_url());
        viewHolder.setTextResort(helper.getResort());
        viewHolder.setTextTitle(helper.getTitle());
        viewHolder.setTextPrice(helper.getPrice());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textTitle, textResort, textPrice;

        private viewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.user_imageView);
            textTitle = itemView.findViewById(R.id.user_title);
            textResort = itemView.findViewById(R.id.user_resort_text);
            textPrice = itemView.findViewById(R.id.user_price_text);
        }

        private void setImageView(String url){
            Glide.with(context).load(url).into(imageView);
        }

        private void setTextTitle(String title){
            textTitle.setText(title);
        }

        private void setTextResort(String resort){
            textResort.setText(resort);
        }

        private void setTextPrice(String price){
            textPrice.setText(price);
        }
    }
}
