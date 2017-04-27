package com.cooldevs.erasmuskit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewTipActivity extends AppCompatActivity {

    private static final String TAG = "NewTipActivity";
    private String tipCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_tip);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.new_tip_toolbar_title);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        final TextView tipCategoryTextView = (TextView) findViewById(R.id.tip_category_selector);
        tipCategoryTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(NewTipActivity.this)
                        .title("Select tip category")
                        .items(Tip.TipCategory.getAllValues())
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                tipCategory = text.toString();
                                Log.d(TAG, "Chosen tip category: " + tipCategory);

                                tipCategoryTextView.setText(tipCategory);

                            }

                        })
                        .show();
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_post_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {

            case R.id.action_save:

                final EditText evTitleEditText =
                        (EditText) findViewById(R.id.tip_title);
                final EditText evContentEditText =
                        (EditText) findViewById(R.id.tip_content);
                String tipTitle = evTitleEditText.getText().toString();
                String tipContent = evContentEditText.getText().toString();


                if (!TextUtils.isEmpty(tipTitle) && !TextUtils.isEmpty(tipContent) && tipCategory != null) {

                    String cityKey = getIntent().getStringExtra("cityKey");
                    long timestamp = System.currentTimeMillis();

                    // Add event to Firebase Database
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("posts").child("tips");
                    ref.push().setValue(new Tip(tipTitle, tipContent, cityKey, timestamp, tipCategory));

                    Toast.makeText(this, R.string.event_saved, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, R.string.uncomplete_data, Toast.LENGTH_LONG).show();
                }

                return true;

            case android.R.id.home:
                finish();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

}
