package eclipse.bcp.listener;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

import eclipse.bcp.BcPlugin;
import eclipse.bcp.constants.BcpConstants;
import eclipse.bcp.handlers.BcpHandler;

public class BcpPrefChangeListener implements IPropertyChangeListener {

	public BcpPrefChangeListener() {
		BcPlugin.getDefault().getPreferenceStore().addPropertyChangeListener(this);
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (BcpConstants.CONFIGURE_SEVERITY.equals(event.getProperty())) {
			try {
				new BcpHandler().execute(null);
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
	}
}
