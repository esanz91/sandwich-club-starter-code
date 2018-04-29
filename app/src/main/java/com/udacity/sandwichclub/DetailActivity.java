package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";

    private static final int DEFAULT_POSITION = -1;
    private static final String DELIMITER = ", ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        final ImageView imageIv = findViewById(R.id.image_iv);
        final View imageScrim = findViewById(R.id.image_scrim);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI(sandwich);

        Picasso.with(this)
                .load(sandwich.getImage())
                .into(imageIv, new Callback() {
                    @Override
                    public void onSuccess() {
                        imageIv.setVisibility(View.VISIBLE);
                        imageScrim.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError() {
                        imageIv.setVisibility(View.GONE);
                        imageScrim.setVisibility(View.GONE);
                    }
                });

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {
        TextView descriptionLabel = findViewById(R.id.description_label);
        TextView descriptionTv = findViewById(R.id.description_tv);
        TextView ingredientsLabel = findViewById(R.id.ingredients_label);
        TextView ingredientsTv = findViewById(R.id.ingredients_tv);
        TextView originLabel = findViewById(R.id.origin_label);
        TextView originTv = findViewById(R.id.origin_tv);
        TextView akaLabel = findViewById(R.id.also_known_label);
        TextView akaTv = findViewById(R.id.also_known_tv);

        bindSandwichInfo(descriptionLabel, descriptionTv, sandwich.getDescription());
        bindSandwichInfo(ingredientsLabel, ingredientsTv,
                TextUtils.join(DELIMITER, sandwich.getIngredients()));
        bindSandwichInfo(originLabel, originTv, sandwich.getPlaceOfOrigin());
        bindSandwichInfo(akaLabel, akaTv,
                TextUtils.join(DELIMITER, sandwich.getAlsoKnownAs()));
    }

    private void bindSandwichInfo(TextView labelView, TextView detailsView, String details) {
        if (TextUtils.isEmpty(details)) {
            labelView.setVisibility(View.GONE);
            detailsView.setVisibility(View.GONE);
            return;
        }

        detailsView.setText(details);
    }
}
