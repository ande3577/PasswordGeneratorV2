package org.dsanderson.passwordgeneratorv2;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
            rootView.findViewById(R.id.clearButton).setOnClickListener(this);
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
                case R.id.clearButton:
                    onClearButtonClicked();
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
            showPassword();
        }



        void onClearButtonClicked() {
            setPasswordValue("");
            hidePassword();
        }

        void setPasswordValue(String password) {
            getPasswordView().setText(password);
        }

        TextView getPasswordView() {
            return (TextView) getView().findViewById(R.id.password);
        }

        void showPassword() {
            getPasswordLayoutView().setVisibility(View.VISIBLE);
        }

        void hidePassword() {
            getPasswordLayoutView().setVisibility(View.GONE);
        }

        RelativeLayout getPasswordLayoutView() {
            return (RelativeLayout) getView().findViewById(R.id.passwordLayout);
        }

    }

}
