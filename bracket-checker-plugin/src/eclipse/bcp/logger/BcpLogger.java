package eclipse.bcp.logger;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import eclipse.bcp.BcPlugin;

public final class BcpLogger {

	private BcpLogger() {
	}

	public static void info(String msg) {

		if (BcPlugin.getDefault() != null) {
			BcPlugin.getDefault().getLog().log(new Status(IStatus.INFO, BcPlugin.PLUGIN_ID, msg));
		}
	}

	public static void error(String msg, Throwable t) {

		if (BcPlugin.getDefault() != null) {
			BcPlugin.getDefault().getLog().log(new Status(IStatus.ERROR, BcPlugin.PLUGIN_ID, msg, t));
		}
	}
}
