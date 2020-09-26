package com.example.teamtest1;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

    private ArrayList<Product> arrayList;
    private Context context;

    public CustomAdapter(ArrayList<Product> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        // 각 아이템들에 대한 매칭
        Glide.with(holder.itemView)
                .load(arrayList.get(position).getImage())
                .into(holder.iv_productImage);

        holder.tv_productBid.setText(arrayList.get(position).getBid());
        holder.tv_productTitle.setText(arrayList.get(position).getTitle());
        holder.tv_productPrice.setText(arrayList.get(position).getPrice());

//        holder.tv_productDetail.setText(arrayList.get(position).getDetail());
//        holder.tv_productSeller.setText(arrayList.get(position).getSeller());
//        holder.tv_productDate.setText(arrayList.get(position).getDate());
//        holder.tv_productDeadline.setText(arrayList.get(position).getDeadline());
//        holder.tv_productCategory.setText(arrayList.get(position).getCategory());
//        holder.tv_productStatus.setText(arrayList.get(position).getStatus());

    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0 );
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_productImage;
        TextView tv_productTitle;
        TextView tv_productBid;
        TextView tv_productPrice;
//        TextView tv_productSeller;
//        TextView tv_productDate;
//        TextView tv_productDeadline;
//        TextView tv_productDetail;
//        TextView tv_productCategory;
//        TextView tv_productStatus;


        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tv_productTitle = itemView.findViewById(R.id.tv_productTitle);
            this.tv_productPrice = itemView.findViewById(R.id.tv_productPrice);
            this.tv_productBid = itemView.findViewById(R.id.tv_productBid);
            this.iv_productImage = itemView.findViewById(R.id.iv_productImage);
//            this.tv_productSeller = itemView.findViewById(R.id.tv_productSeller);
//            this.tv_productDate = itemView.findViewById(R.id.tv_productDate);
//            this.tv_productDeadline = itemView.findViewById(R.id.tv_productDeadline);
//            this.tv_productCategory = itemView.findViewById(R.id.tv_productCategory);
//            this.tv_productStatus = itemView.findViewById(R.id.tv_productStatus);
//            this.tv_productDetail = itemView.findViewById(R.id.tv_productDetail);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                    Bitmap bitmap = ((BitmapDrawable)iv_productImage.getDrawable()).getBitmap();
//                    float scale = (float)(1024/(float)bitmap.getWidth());
//                    int image_w = (int) (bitmap.getWidth() * scale);
//                    int image_h = (int) (bitmap.getHeight() * scale);
//                    Bitmap resize = Bitmap.createScaledBitmap(bitmap, image_w, image_h,true);
//
//                    resize.compress(Bitmap.CompressFormat.JPEG,100,stream);
//                    byte[] byteArray = stream.toByteArray();

                    int position = getAdapterPosition();

                    Intent intent = new Intent(v.getContext(), Sub.class);

                    intent.putExtra("image", arrayList.get(position).getImage());
                    intent.putExtra("title", arrayList.get(position).getTitle());
                    intent.putExtra("price", arrayList.get(position).getPrice());
                    intent.putExtra("bid", arrayList.get(position).getBid());
                    intent.putExtra("detail", arrayList.get(position).getDetail());

                    v.getContext().startActivity(intent);
                }
            });


        }
    }
}

