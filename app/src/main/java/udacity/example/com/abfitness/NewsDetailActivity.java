package udacity.example.com.abfitness;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsDetailActivity extends AppCompatActivity {

    private static final String TAG = NewsDetailActivity.class.getSimpleName();
    private static final String HEADER = "header";
    private static final String LOGO_URL = "logo_url";
    private static final String ARTICLE = "article";

    @BindView(R.id.photo)
    ImageView mArticlePhoto;

    @BindView(R.id.article_header)
    TextView mHeader;

    @BindView(R.id.article_body)
    TextView mBody;

    @BindView(R.id.share_fab)
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_news_detail);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent != null) {
            String header = intent.getStringExtra(HEADER);
            String picUrl = intent.getStringExtra(LOGO_URL);
            String article = intent.getStringExtra(ARTICLE);

            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.color.colorPrimaryLight)
                    .error(R.mipmap.ic_launcher_round);

            Glide.with(this).load(picUrl).apply(options).into(mArticlePhoto);

            mHeader.setText(header);
            mBody.setText(article);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareButtonIntent();
            }
        });

    }

    private void shareButtonIntent() {
        String article = mBody.getText().toString();
        startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(article)
                .getIntent(), getString(R.string.action_share)));
    }

}
