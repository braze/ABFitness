package udacity.example.com.abfitness;

import android.content.ContentUris;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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

import java.util.ArrayList;

import udacity.example.com.abfitness.adapters.BaseListAdapter;
import udacity.example.com.abfitness.async.tasks.FetchJsonAsyncTask;
import udacity.example.com.abfitness.data.MeContract.UserEntry;
import udacity.example.com.abfitness.interfaces.OnAdapterClickHandler;
import udacity.example.com.abfitness.utils.CircleTransform;
import udacity.example.com.abfitness.utils.JsonUtils;

import static udacity.example.com.abfitness.MainActivity.EXTRA_EMAIL;
import static udacity.example.com.abfitness.MainActivity.EXTRA_NAME;
import static udacity.example.com.abfitness.MainActivity.EXTRA_PHOTO_URI;

public class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnAdapterClickHandler {

    private static final String TAG = BaseActivity.class.getSimpleName();
    private static final String BUNDLE_RECYCLER_LAYOUT = "baseListFragment_recycler_layout";
    private static final String ADAPTER_LIST = "baseListFragment_adapter_list";
    public static final String ARG_INTENT_EXERCISES = "argument_intent_exercises";
    public static final String LOGO = "logo";
    public static final String USER_NAME = "username";
    public static final String EMAIL = "email";

    private View mNavHeader;
    private ImageView mImgProfile;
    private TextView mUserName;
    private TextView mUserEmail;
    public ArrayList<String> mBaseList;

    private SharedPreferences mPreferences;
    private RecyclerView mRecyclerView;
    private Parcelable mSavedRecyclerLayoutState;
    private BaseListAdapter mAdapter;

    private String mName;
    private String mLogo;
    private String mMail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ((AppCompatActivity)this).getSupportActionBar().setTitle("AB Fitness");

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
            mName = savedInstanceState.getString(USER_NAME);
            mLogo = savedInstanceState.getString(LOGO);
            mMail = savedInstanceState.getString(EMAIL);

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
    protected void onResume() {
        super.onResume();
        //fill navigation view header with user data
        loadUserDataToNavigationDrawer();
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
            // Handle the ME action
            Intent intent = new Intent(BaseActivity.this, EditorActivity.class);
            Uri currentPetUri = ContentUris.withAppendedId(UserEntry.CONTENT_URI, 1);
            intent.setData(currentPetUri);
            startActivity(intent);
        } else if (id == R.id.nav_meal_plan) {
            //getting mealPlan
//            String jsonString = NetworkUtils.getSharedPreferences().getString(THE_JSON,"");
//            JsonUtils.getMealPlan(jsonString);
            Intent intent = new Intent(BaseActivity.this, MealPlanActivity.class);
            Uri currentPetUri = ContentUris.withAppendedId(UserEntry.CONTENT_URI, 1);
            intent.setData(currentPetUri);
            startActivity(intent);

        } else if (id == R.id.nav_news) {
            startActivity(new Intent(BaseActivity.this, NewsActivity.class));

        } else if (id == R.id.nav_feedback) {
            //Send email
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:yabraze@gmail.com"));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "AB Fitness");
            startActivity(Intent.createChooser(emailIntent, "Send feedback"));

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
            mName = userDataIntent.getStringExtra(EXTRA_NAME);
            mLogo = userDataIntent.getStringExtra(EXTRA_PHOTO_URI);
            mMail = userDataIntent.getStringExtra(EXTRA_EMAIL);

            RequestOptions options = new RequestOptions();
            options.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .transform(new CenterCrop())
                    .transform(new CircleTransform(this));

            Glide.with(this).load(mLogo)
                    .apply(options)
                    .into(mImgProfile);

            mUserName.setText(mName);
            mUserEmail.setText(mMail);

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mAdapter != null){
            outState.putStringArrayList(ADAPTER_LIST, mAdapter.getBaseList());
            outState.putParcelable(BUNDLE_RECYCLER_LAYOUT, mRecyclerView.getLayoutManager().onSaveInstanceState());
            outState.putString(USER_NAME, mName);
            outState.putString(LOGO, mLogo);
            outState.putString(EMAIL, mMail);
        }
    }

    @Override
    public void onClick(int position, String posName) {
        Intent intent = new Intent(this, ExercisesActivity.class);
        intent.putExtra(ARG_INTENT_EXERCISES, posName);
        startActivity(intent);
    }
}
