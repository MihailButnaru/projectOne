package com.burn.burn.controller.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.burn.burn.R;
import com.burn.burn.controller.profile.Profile;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private static final String TAG = "burn.ProfileFragment";

    private String strGender;

    private EditText fullname;
    private EditText city;
    private EditText email;
    private EditText phone;
    private Spinner gender;
    private Button btnSave;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_profile, container,
                false);

        // Acquire components from view
        fullname = fragmentView.findViewById(R.id.txt_fullname);
        city = fragmentView.findViewById(R.id.txt_city);
        email = fragmentView.findViewById(R.id.txt_email);
        phone = fragmentView.findViewById(R.id.txt_phone);
        gender = fragmentView.findViewById(R.id.spinner_gender);
        btnSave = fragmentView.findViewById(R.id.btn_save);

        // Clear dummy text from view
        fullname.setText("");
        city.setText("");
        email.setText("");
        phone.setText("");

        // Set up 'Gender' dropdown menu
        // ...create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter
                .createFromResource(getActivity().getBaseContext(),
                R.array.gender_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        gender.setAdapter(adapter);

        // Gender dropdown listener
        gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                strGender = (String) parent.getItemAtPosition(pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Do nothing ...
            }
        });

        // Save button listener
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFormValid()) {
                    Profile user = new Profile();

                    user.setFullname(fullname.getText().toString());
                    user.setCity(city.getText().toString());
                    user.setEmail(email.getText().toString());
                    user.setPhone(phone.getText().toString());
                    user.setGender(strGender);

                    Profile.save(user);
                }

                Toast.makeText(getActivity().getBaseContext(),
                        "Saved.", Toast.LENGTH_SHORT).show();
            }
        });


        return fragmentView;
    }

    /**
     * Validates user entered text in form components
     * @return
     */
    private boolean isFormValid() {
        return true; // TODO: Validate input
    }

}
