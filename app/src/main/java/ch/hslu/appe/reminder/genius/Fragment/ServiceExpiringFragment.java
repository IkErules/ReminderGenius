package ch.hslu.appe.reminder.genius.Fragment;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ch.hslu.appe.reminder.genius.DB.Entity.Contact;
import ch.hslu.appe.reminder.genius.DB.Entity.Installation;
import ch.hslu.appe.reminder.genius.DB.Entity.ProductCategory;
import ch.hslu.appe.reminder.genius.Notifier.InstallationNotifier;
import ch.hslu.appe.reminder.genius.R;

public class ServiceExpiringFragment extends Fragment {
    private static final String INSTALLATION = "installation.to.show";
    private static final String CONTACT = "contact.to.show";
    private static final String PRODUCT_CATEGORY = "productcategory.to.show";

    private Installation installation;
    private Contact contact;
    private ProductCategory productCategory;

    public ServiceExpiringFragment() {}

    public static ServiceExpiringFragment newInstance(Installation installation, Contact contact, ProductCategory productCategory) {
        ServiceExpiringFragment fragment = new ServiceExpiringFragment();
        Bundle args = new Bundle();
        args.putParcelable(INSTALLATION, installation);
        args.putParcelable(CONTACT, contact);
        args.putParcelable(PRODUCT_CATEGORY, productCategory);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.installation = getArguments().getParcelable(INSTALLATION);
            this.contact = getArguments().getParcelable(CONTACT);
            this.productCategory = getArguments().getParcelable(PRODUCT_CATEGORY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragment = inflater.inflate(R.layout.fragment_service_expiring, container, false);

        Button sendMailBtn = fragment.findViewById(R.id.expiring_service_fragment_send_email_button);
        sendMailBtn.setOnClickListener(view -> {
            onSendEmailButtonClickedListener();
        });

        Button sendSmsBtn = fragment.findViewById(R.id.expiring_service_fragment_send_sms_button);
        sendSmsBtn.setOnClickListener(view -> {
            onSendSmsButtonClickedListener();
        });

        return fragment;
    }

    private void onSendSmsButtonClickedListener() {
        Log.d("ServiceExpiringFragment", "Sending Sms");

        Resources res = getResources();
        String content = String.format(res.getString(R.string.send_sms_content), this.installation.getFriendlyInstallationDateAsString());

        InstallationNotifier.notifyBySms(getActivity(), this.contact.getPhone(), content);
    }

    private void onSendEmailButtonClickedListener() {
        Log.d("ServiceExpiringFragment", "Sending Email");
        String[] mailAddresses = {this.contact.getMail()};

        Resources res = getResources();
        String content = String.format(res.getString(R.string.send_email_content), this.installation.getFriendlyInstallationDateAsString());

        InstallationNotifier.notifyByEmail(getActivity(), mailAddresses , getString(R.string.send_email_subject), content);
    }


}
