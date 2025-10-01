package eclipse.bcp.preferences;

import org.eclipse.core.resources.IMarker;
import org.eclipse.jface.preference.IPreferenceStore;

import eclipse.bcp.BcPlugin;
import eclipse.bcp.constants.BcpConstants;

public final class BcpPrefs {

	private static final IPreferenceStore PREFS = BcPlugin.getDefault().getPreferenceStore();

	public static int getSeverity() {

		String severity = PREFS.getString(BcpConstants.CONFIGURE_SEVERITY);

		switch (severity) {
		case BcpConstants.ERROR:
			return IMarker.SEVERITY_ERROR;
		case BcpConstants.WARN:
			return IMarker.SEVERITY_WARNING;
		case BcpConstants.INFO:
			return IMarker.SEVERITY_INFO;
		default:
			return IMarker.SEVERITY_WARNING;
		}

	}

}
