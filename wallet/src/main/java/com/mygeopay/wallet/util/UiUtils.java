package com.mygeopay.wallet.util;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.mygeopay.core.coins.CoinType;
import com.mygeopay.core.util.GenericUtils;
import com.mygeopay.wallet.AddressBookProvider;
import com.mygeopay.wallet.R;
import com.mygeopay.wallet.ui.EditAddressBookEntryFragment;

import org.bitcoinj.core.Address;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author John L. Jegutanis
 */
public class UiUtils {
    private static final Logger log = LoggerFactory.getLogger(UiUtils.class);

    static public void share(Activity activity, String text) {
        ShareCompat.IntentBuilder builder = ShareCompat.IntentBuilder.from(activity)
                .setType("text/plain")
                .setText(text);

        activity.startActivity(Intent.createChooser(
                builder.getIntent(),
                activity.getString(R.string.action_share)));
    }

    public static class AddressActionModeCallback implements ActionMode.Callback {
        private final Address address;
        private final Context context;
        private final FragmentManager fragmentManager;


        public AddressActionModeCallback(final Address address,
                                         final Context context,
                                         final FragmentManager fragmentManager) {
            this.address = address;
            this.context = context;
            this.fragmentManager = fragmentManager;
        }

        public Address getAddress() {
            return address;
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.address_options, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            final String label = AddressBookProvider.resolveLabel(context, address);
            mode.setTitle(label != null ? label : GenericUtils.addressSplitToGroups(address));
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_edit_label:
                    EditAddressBookEntryFragment.edit(fragmentManager, address);
                    mode.finish();
                    return true;
                case R.id.action_copy:
                    UiUtils.copy(context, address.toString());
                    mode.finish();
                    return true;
            }

            return false;
        }

        @Override public void onDestroyActionMode(ActionMode actionMode) { }
    }

    public static ActionMode startActionMode(final Activity activity, final ActionMode.Callback callback) {
        if (activity == null || !(activity instanceof ActionBarActivity)) {
            log.warn("To show action mode, your activity must extend " + ActionBarActivity.class);
            return null;
        }
        return ((ActionBarActivity) activity).startSupportActionMode(callback);
    }

    public static ActionMode startAddressActionMode(final Address address,
                                                    final Activity activity,
                                                    final FragmentManager fragmentManager) {
        return startActionMode(activity,
                new AddressActionModeCallback(address, activity, fragmentManager));
    }

    public static void copy(Context context, String string) {
        Object clipboardService = context.getSystemService(Context.CLIPBOARD_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ClipboardManager clipboard = (ClipboardManager) clipboardService;
            clipboard.setPrimaryClip(ClipData.newPlainText("simple text", string));
        } else {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) clipboardService;
            clipboard.setText(string);
        }
        Toast.makeText(context, R.string.copied_to_clipboard, Toast.LENGTH_SHORT).show();
    }

    public static void setVisible(View view) {
        setVisibility(view, View.VISIBLE);
    }

    public static void setInvisible(View view) {
        setVisibility(view, View.INVISIBLE);
    }

    public static void setGone(View view) {
        setVisibility(view, View.GONE);
    }

    public static void setVisibility(View view, int visibility) {
        if (view.getVisibility() != visibility) view.setVisibility(visibility);
    }
}
