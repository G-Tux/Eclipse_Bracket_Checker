package eclipse.bcp.listener;

import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocumentListener;

import eclipse.bcp.handlers.BcpHandler;

public class BcpDocumentListener implements IDocumentListener {

	@Override
	public void documentChanged(DocumentEvent event) {
		try {
			new BcpHandler().execute(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void documentAboutToBeChanged(DocumentEvent event) {
	}
}
