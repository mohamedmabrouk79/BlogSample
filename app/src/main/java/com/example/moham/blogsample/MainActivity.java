package com.example.moham.blogsample;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
    private RecyclerView  mRecyclerView;
    private DatabaseReference mReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView=(RecyclerView)findViewById(R.id.blog_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mReference= FirebaseDatabase.getInstance().getReference().child("blogs");
        mReference.keepSynced(true);
        FirebaseRecyclerAdapter<Blog,PostHolder> recyclerAdapter=new FirebaseRecyclerAdapter<Blog, PostHolder>(
                Blog.class,R.layout.post_view,PostHolder.class,mReference
        ) {
            @Override
            protected void populateViewHolder(PostHolder viewHolder, Blog model, int position) {
            viewHolder.bindData(model,getApplicationContext());
            }
        };

        mRecyclerView.setAdapter(recyclerAdapter);

    }

    /******** blog post holder **************/
    static class PostHolder extends RecyclerView.ViewHolder{
        private ImageView mImageView;
        private TextView mTitle;
        private TextView mDesc;
        public PostHolder(View itemView) {
            super(itemView);
            mImageView= (ImageView) itemView.findViewById(R.id.image_post);
            mTitle= (TextView) itemView.findViewById(R.id.title_of_post);
            mDesc= (TextView) itemView.findViewById(R.id.description_of_post);
        }

        public void bindData(final Blog blog, final Context context){
            mTitle.setText(blog.getTitle());
            mDesc.setText(blog.getDesc());
            //Picasso.with(context).load(blog.getImage()).into(mImageView);
            Picasso.with(context).load(blog.getImage()).networkPolicy(NetworkPolicy.OFFLINE).into(mImageView, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(context).load(blog.getImage()).into(mImageView);
                }
            });

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

           getMenuInflater().inflate(R.menu.menu,menu);
         return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.ic_add:
                startActivity(new Intent(MainActivity.this,PostActivity.class));
                return  true;
            case R.id.settings:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }
}
