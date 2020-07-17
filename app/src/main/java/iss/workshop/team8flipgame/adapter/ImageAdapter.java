package iss.workshop.team8flipgame.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

import iss.workshop.team8flipgame.activity.GameActivity;
import iss.workshop.team8flipgame.R;
import iss.workshop.team8flipgame.activity.ImagePickingActivity;
import iss.workshop.team8flipgame.model.Image;
import iss.workshop.team8flipgame.model.Score;
import iss.workshop.team8flipgame.service.DBService;

public class ImageAdapter extends BaseAdapter{

    private  Context mContext;
    private  ArrayList<Image> images;
    public ArrayList<Bitmap> barray = new ArrayList<>();
    ArrayList<ImageView> seleted_view = new ArrayList<>();
    boolean disableFlip;

    private static final int NUM_OF_CARDS = 6;
    int numOfAttempts = 0;
    int totalTime = 0;

    private static int MASK_HINT_COLOR = 0x99ffffff;
    public ImageAdapter(Context mContext, ArrayList<Image> images){
        this.mContext = mContext;
        this.images=images;
    }

    @Override
    public int getCount(){
        return images.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int pos, View view, ViewGroup viewGroup) {
        if(mContext instanceof ImagePickingActivity){

            final Image image = images.get(pos);
            image.setPosID(pos);
            if (view == null) {
                final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
                view = layoutInflater.inflate(R.layout.images, null);
            }
            final ImageView imageView1 = view.findViewById(R.id.image);
            imageView1.setImageBitmap(image.getBitmap());
            if (imageView1.getColorFilter() != null) {
                Log.i("ColorFilter","cleared after new fetch.");
                imageView1.clearColorFilter();
            }
        }

        if(mContext instanceof GameActivity){

            final Image image = images.get(pos);
            image.setPosID(pos);

            if (view == null) {
                final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
                view = layoutInflater.inflate(R.layout.images2, null);
            }

            final ImageView imageView2 =view.findViewById(R.id.image2);

            final Handler handler = new Handler();
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    seleted_view.get(1).setImageBitmap(null);
                    seleted_view.get(1).setClickable(true);
                    seleted_view.get(0).setClickable(true);
                    seleted_view.get(0).setImageBitmap(null);
                    barray.clear();
                    seleted_view.clear();
                }
            };
        }
        return view;
    }

    public int calculateScore(int totalTime,int numOfAttempts){
        return (5 * NUM_OF_CARDS) + (500 / numOfAttempts) + (5000 / totalTime);
    }

    public void finishedGame(int totalTime,int numOfAttempts){
        int totalScore = calculateScore(60,15);
        Score scoreObj = new Score("Theingi",totalScore);
        DBService db = new DBService(mContext);
        db.addScore(scoreObj);
    }

    public void updateItemList(ArrayList<Image> newItemList) {
        this.images = newItemList;
        notifyDataSetChanged();
    }

}
