package com.example.android1.shareactionprovider;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android1.shareactionprovider.content.ContentItem;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {


    private final ArrayList<ContentItem> mItems = getSampleContent();

    private final PagerAdapter mPagerAdapter = new PagerAdapter() {
        LayoutInflater mInflater;

        @Override
        public int getCount() {
            return mItems.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            if (mInflater == null) {
                mInflater = LayoutInflater.from(MainActivity.this);
            }


            final ContentItem item = mItems.get(position);


            switch (item.contentType) {
                case ContentItem.CONTENT_TYPE_TEXT: {

                    TextView tv = (TextView) mInflater
                            .inflate(R.layout.item_text, container, false);


//                    tv.setText(item.contentResourceId);


                    container.addView(tv);
                    return tv;
                }
                case ContentItem.CONTENT_TYPE_IMAGE: {

                    ImageView iv = (ImageView) mInflater
                            .inflate(R.layout.item_image, container, false);


                    iv.setImageURI(item.getContentUri());


                    container.addView(iv);
                    return iv;
                }
            }

            return null;
        }
    };

    private ShareActionProvider mShareActionProvider;

    private final ViewPager.OnPageChangeListener mOnPageChangeListener
            = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            // NO-OP
        }

        @Override
        public void onPageSelected(int position) {
            setShareIntent(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            // NO-OP
        }
    };

    static ArrayList<ContentItem> getSampleContent() {
        ArrayList<ContentItem> items = new ArrayList<ContentItem>();

        items.add(new ContentItem(ContentItem.CONTENT_TYPE_IMAGE, "photo_1.jpg"));
        items.add(new ContentItem(ContentItem.CONTENT_TYPE_TEXT, "photo 1"));
        items.add(new ContentItem(ContentItem.CONTENT_TYPE_TEXT, "photo 2"));
        items.add(new ContentItem(ContentItem.CONTENT_TYPE_IMAGE, "photo_2.jpg"));
        items.add(new ContentItem(ContentItem.CONTENT_TYPE_TEXT, "photo 3"));
        items.add(new ContentItem(ContentItem.CONTENT_TYPE_IMAGE, "photo_3.jpg"));

        return items;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_main);
        ViewPager vp = (ViewPager) findViewById(R.id.viewpager);
        vp.addOnPageChangeListener(mOnPageChangeListener);
        vp.setAdapter(mPagerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem shareItem = menu.findItem(R.id.menu_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        int currentViewPagerItem = ((ViewPager) findViewById(R.id.viewpager)).getCurrentItem();
        setShareIntent(currentViewPagerItem);

        return super.onCreateOptionsMenu(menu);
    }

    private void setShareIntent(int position) {
        if (mShareActionProvider != null) {
            ContentItem item = mItems.get(position);
            Intent shareIntent = item.getShareIntent(MainActivity.this);
            mShareActionProvider.setShareIntent(shareIntent);
        }

    }

}