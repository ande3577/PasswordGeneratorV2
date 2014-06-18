package org.dsanderson.passwordgeneratorv2;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;
import android.widget.TextView;

import org.dsanderson.password_generator.core.PasswordGenerator;


public class PasswordGeneratorActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_generator);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements View.OnClickListener {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_password_generator, container, false);
            rootView.findViewById(R.id.generateButton).setOnClickListener(this);
            ((TextView) rootView.findViewById(R.id.length)).setText(Integer.toString(PasswordGenerator.DEFAULT_LENGTH));
            return rootView;
        }

        TextView getLengthView() {
            return (TextView) getView().findViewById(R.id.length);
        }

        int getLengthValue() {
            return Integer.parseInt(getLengthView().getText().toString());
        }

        String getKeywordValue() {
            return ((TextView) getView().findViewById(R.id.keyword)).getText().toString();
        }

        @Override
        public void onClick(View view) {
            switch(view.getId()) {
                case R.id.generateButton:
                    onGenerateButtonClicked();
                    break;
            }
        }

        void onGenerateButtonClicked() {
            PasswordGenerator passwordGenerator = new PasswordGenerator();
            String password;
            try {
                password = passwordGenerator.generate(getLengthValue(), getKeywordValue());
            } catch (Exception ex) {
                password = ex.getMessage();
            }
            setPasswordValue(password);
        }

        void setPasswordValue(String password) {
            getPasswordView().setText(password);
        }

        TextView getPasswordView() {
            return (TextView) getView().findViewById(R.id.password);
        }

    }

}
