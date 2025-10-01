package eclipse.bcp.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import eclipse.bcp.BcPlugin;
import eclipse.bcp.constants.BcpConstants;

public class PrefInitializer extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = BcPlugin.getDefault().getPreferenceStore();
		store.setDefault(BcpConstants.CONFIGURE_SEVERITY, BcpConstants.WARN);
	}

}
