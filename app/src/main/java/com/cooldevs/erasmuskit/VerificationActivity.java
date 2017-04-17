package com.cooldevs.erasmuskit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class VerificationActivity extends AppCompatActivity {

    private static final String TAG = "VerificationActivity";

    private ImageView verifyImage;
    private TextView verifyText;
    private Button verifyButton;
    private ProgressBar progressBar;

    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.register_toolbar_title);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        user = FirebaseAuth.getInstance().getCurrentUser();

        verifyImage = (ImageView) findViewById(R.id.verification_state_image);
        verifyText = (TextView) findViewById(R.id.verification_state_text);
        verifyButton = (Button) findViewById(R.id.verification_button);
        progressBar = (ProgressBar) findViewById(R.id.verification_progress_bar);

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VerificationActivity.this, CitiesActivity.class));
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.verification_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_refresh:
                progressBar.setVisibility(View.VISIBLE);

                user.reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "User has verified his account: " + user.isEmailVerified());
                        if (user.isEmailVerified()) {
                            verifyImage.setImageResource(R.drawable.ic_verified_user_black_24dp);
                            verifyText.setText(getString(R.string.verified_account));
                            verifyButton.setEnabled(true);
                        }

                        progressBar.setVisibility(View.GONE);
                    }
                });
                break;

            case android.R.id.home:
                setResult(RESULT_CANCELED);
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
