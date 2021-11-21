package com.booster.investortypescheck.view;


import static com.booster.investortypescheck.parse.ParseUtils.parseJason;
import static com.booster.investortypescheck.view.utils.Constants.SCORE_KEY_SP;

import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.preference.PreferenceManager;

import com.booster.investortypescheck.R;
import com.booster.investortypescheck.parse.ParseUtils;
import com.booster.investortypescheck.view.base.BaseFragmentActivity;
import com.booster.investortypescheck.view.base.InvestorTypeFactory;
import com.booster.investortypescheck.view.drawer.BoosterActionBarDrawerToggle;
import com.booster.investortypescheck.view.drawer.DrawerListAdapter;
import com.booster.investortypescheck.view.fragment.EmailFragment;
import com.booster.investortypescheck.view.fragment.IntroductionFragment;
import com.booster.investortypescheck.view.fragment.QuestionFragment;
import com.booster.investortypescheck.view.fragment.TypeFragment;

/**The only activity in APP.
 * Customer can select the item in menu, then show the views.
 * @author sgao
 */
public class MainActivity extends BaseFragmentActivity {
    private TypeFragment mTypeFragment;
    private QuestionFragment mQuestionFragment;
    private EmailFragment mEmailFragment;
    private Button questionBt;
    private Button submitBt;
    private ListView mDrawerList;
    private DrawerListAdapter mDrawerAdapter;
    private BoosterActionBarDrawerToggle mDrawerToggle;
    FragmentManager fragmentManager;

    @Override
    public void initView(LayoutInflater inflater) {
        setContentView(R.layout.main_layout);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerList = findViewById(R.id.left_drawer);


        mDrawerAdapter = new DrawerListAdapter(this);
        mDrawerList.setAdapter(mDrawerAdapter);

        mDrawerToggle = new BoosterActionBarDrawerToggle(this, mDrawerLayout);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerList.setOnItemClickListener(new InvestorTypeClickListener());

        questionBt = findViewById(R.id.question_bt);
        questionBt.setOnClickListener(new QuestionClickListener());
        submitBt = findViewById(R.id.submit_bt);
        submitBt.setOnClickListener(new SubmitClickListener());

        parseJason(ParseUtils.getJsonString(this));
    }

    @Override
    public void loadData() {
        fragmentManager = getSupportFragmentManager();

        IntroductionFragment fragment = new IntroductionFragment();
        fragmentManager.beginTransaction().replace(R.id.list_container,fragment).commit();
    }

    private void selectInvestorType(final int position, String type) {

        mTypeFragment = new InvestorTypeFactory().getFragment(type, this);
        if(mTypeFragment!=null) {
            fragmentManager.beginTransaction().replace(R.id.list_container,mTypeFragment).commit();
        }
        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(findViewById(R.id.drawer));
    }


    private void selectQuestion() {
        mQuestionFragment = new QuestionFragment();
        fragmentManager.beginTransaction().replace(R.id.list_container,mQuestionFragment).commit();
        // update selected item and title, then close the drawer
        mDrawerLayout.closeDrawer(findViewById(R.id.drawer));
    }


    private void selectSubmit() {
        mEmailFragment = new EmailFragment();
        fragmentManager.beginTransaction().replace(R.id.list_container,mEmailFragment).commit();
        // update selected item and title, then close the drawer
        mDrawerLayout.closeDrawer(findViewById(R.id.drawer));
    }

    private class QuestionClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            selectQuestion();
        }
    }

    private class SubmitClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
            if(prefs.getInt(SCORE_KEY_SP, 0) == 0) {
                Toast.makeText(MainActivity.this, "Please do the questionnaire first. Thanks", Toast.LENGTH_SHORT).show();
                return;
            }
            selectSubmit();
            prefs.edit().remove(SCORE_KEY_SP).apply();
        }
    }

    private class InvestorTypeClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            CharSequence optionName = ((TextView) view.findViewById(R.id.textView1)).getText();
            selectInvestorType(position, optionName.toString());
            Log.v("GXL", optionName.toString());
        }
    }

}
