package udacity.example.com.abfitness;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import udacity.example.com.abfitness.Utils.CircleTransform;
import udacity.example.com.abfitness.Utils.JsonUtils;
import udacity.example.com.abfitness.Utils.NetworkUtils;
import udacity.example.com.abfitness.adapters.BaseListAdapter;
import udacity.example.com.abfitness.async.tasks.FetchJsonAsyncTask;
import udacity.example.com.abfitness.interfaces.OnAdapterClickHandler;

import static udacity.example.com.abfitness.MainActivity.EXTRA_EMAIL;
import static udacity.example.com.abfitness.MainActivity.EXTRA_NAME;
import static udacity.example.com.abfitness.MainActivity.EXTRA_PHOTO_URI;
import static udacity.example.com.abfitness.Utils.NetworkUtils.THE_JSON;

public class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnAdapterClickHandler {

    private static final String TAG = BaseActivity.class.getSimpleName();
    private static final String BUNDLE_RECYCLER_LAYOUT = "baseListFragment_recycler_layout";
    private static final String ADAPTER_LIST = "baseListFragment_adapter_list";
    public static final String ARG_INTENT_EXERCISES = "argument_intent_exercises";

    private View mNavHeader;
    private ImageView mImgProfile;
    private TextView mUserName;
    private TextView mUserEmail;
    public ArrayList<String> mBaseList;

    private SharedPreferences mPreferences;
    private RecyclerView mRecyclerView;
    private Parcelable mSavedRecyclerLayoutState;
    private BaseListAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ((AppCompatActivity)this).getSupportActionBar().setTitle("AB Fitness");

//        toolbar.setTitle("AB Fitness");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Navigation view header
        mNavHeader = navigationView.getHeaderView(0);
        mUserName = (TextView) mNavHeader.findViewById(R.id.nav_drawer_user_name);
        mUserEmail = (TextView) mNavHeader.findViewById(R.id.nav_drawer_email);
//        imgNavHeaderBg = (ImageView) mNavHeader.findViewById(R.id.img_header_bg);
        mImgProfile = (ImageView) mNavHeader.findViewById(R.id.nav_drawer_iv);

        //fill navigation view header with user data
        loadUserDataToNavigationDrawer();

        //start Async task
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        new FetchJsonAsyncTask(mPreferences).execute();

        //RecyclerView work
        mRecyclerView = (RecyclerView) findViewById(R.id.base_list_rv);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new BaseListAdapter(this);

        if (savedInstanceState != null) {
            mSavedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);

            mRecyclerView.getLayoutManager().onRestoreInstanceState(mSavedRecyclerLayoutState);
            ArrayList<String> baseList = savedInstanceState.getStringArrayList(ADAPTER_LIST);
            mAdapter.setBaseList(baseList);
        } else {
            mBaseList = JsonUtils.getBaseList();
            mAdapter.setBaseList(mBaseList);
        }
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_me) {
            // Handle the me action
        } else if (id == R.id.nav_meal_plan) {
            Calendar calendar = Calendar.getInstance();
            Date date = calendar.getTime();
            String dayOfWeek = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime());
            String jsonString = NetworkUtils.getSharedPreferences().getString(THE_JSON,"");
            JsonUtils.getNews(jsonString, dayOfWeek);

        } else if (id == R.id.nav_news) {
            // TODO: 2/13/19 get news
            startActivity(new Intent(BaseActivity.this, NewsActivity.class));

        } else if (id == R.id.nav_feedback) {
            // TODO: 2/13/19 write email

        } else if (id == R.id.btn_sign_out) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //fill navigation view header with user data
    private void loadUserDataToNavigationDrawer() {
        Intent userDataIntent = getIntent();
        if (userDataIntent != null) {
            String name = userDataIntent.getStringExtra(EXTRA_NAME);
            String pic = userDataIntent.getStringExtra(EXTRA_PHOTO_URI);
            String mail = userDataIntent.getStringExtra(EXTRA_EMAIL);

            RequestOptions options = new RequestOptions();
            options.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .transform(new CenterCrop())
                    .transform(new CircleTransform(this));

            Glide.with(this).load(pic)
                    .apply(options)
                    .into(mImgProfile);

            mUserName.setText(name);
            mUserEmail.setText(mail);

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mAdapter != null){
            outState.putStringArrayList(ADAPTER_LIST, mAdapter.getBaseList());
            outState.putParcelable(BUNDLE_RECYCLER_LAYOUT, mRecyclerView.getLayoutManager().onSaveInstanceState());
        }
    }


    @Override
    public void onClick(int position, String posName) {
        Intent intent = new Intent(this, Exercises.class);
        intent.putExtra(ARG_INTENT_EXERCISES, posName);
        startActivity(intent);
    }
}
