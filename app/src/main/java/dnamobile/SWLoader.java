package dnamobile;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.besome.sketch.MainActivity;
import com.sketchware.remodgdx.R;

import java.util.Objects;

public class SWLoader extends AppCompatActivity {
    MainActivity m = new MainActivity();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        m.tryLoadingCustomizedAppStrings(this);
        setContentView(R.layout.main);
        m.setSupportActionBar(findViewById(R.id.toolbar));

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);
        m.viewPager = findViewById(R.id.viewpager);
        m.viewPager.setAdapter(new MainActivity.PagerAdapter(getSupportFragmentManager()));
        m.viewPager.addOnPageChangeListener(m);
    }
}