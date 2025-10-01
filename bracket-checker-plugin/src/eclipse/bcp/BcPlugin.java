package eclipse.bcp;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.texteditor.ITextEditor;
import org.osgi.framework.BundleContext;

import eclipse.bcp.listener.BcpDocumentListener;
import eclipse.bcp.listener.BcpPrefChangeListener;
import eclipse.bcp.logger.BcpLogger;

/**
 * The activator class controls the plug-in life cycle
 */
public class BcPlugin extends AbstractUIPlugin implements IStartup {

	public static final String PLUGIN_ID = "bracket-checker-plugin";

	private static BcPlugin plugin;

	private final Map<ITextEditor, BcpDocumentListener> editorListeners = Collections
			.synchronizedMap(new WeakHashMap<>());

	public BcPlugin() {
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

		BcpLogger.info("BcPlugin started.");
		new BcpPrefChangeListener();
	}

	@Override
	public void earlyStartup() {
		Display.getDefault().asyncExec(() -> {
			initializeEditorListeners();
		});
	}

	private void initializeEditorListeners() {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();

		if (window == null) {
			BcpLogger.info("No workbench window available at early startup.");
			return;
		}

		IWorkbenchPage page = window.getActivePage();

		if (page == null) {
			BcpLogger.info("No active page available at early startup.");
			return;
		}

		for (IEditorReference editorRef : page.getEditorReferences()) {
			IEditorPart editorPart = editorRef.getEditor(false);
			if (isXmlOrXhtmlEditor(editorPart)) {
				attachListenerToEditor((ITextEditor) editorPart);
			}
		}

		page.addPartListener(new IPartListener2() {
			@Override
			public void partOpened(IWorkbenchPartReference partRef) {
				IWorkbenchPart workbenchPart = partRef.getPart(false);
				if (isXmlOrXhtmlEditor(workbenchPart)) {
					attachListenerToEditor((ITextEditor) workbenchPart);
				}
			}

			@Override
			public void partClosed(IWorkbenchPartReference partRef) {
				IWorkbenchPart workbenchPart = partRef.getPart(false);
				if (isXmlOrXhtmlEditor(workbenchPart)) {
					detachListenerFromEditor((ITextEditor) workbenchPart);
				}
			}

		});
	}

	private void attachListenerToEditor(ITextEditor textEditor) {
		if (editorListeners.containsKey(textEditor)) {
			return;
		}
		IDocument doc = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput());
		BcpDocumentListener listener = new BcpDocumentListener();
		doc.addDocumentListener(listener);
		editorListeners.put(textEditor, listener);
		BcpLogger.info("Attached BcpDocumentListener to editor: " + textEditor.getTitle());
	}

	private void detachListenerFromEditor(ITextEditor textEditor) {
		BcpDocumentListener listener = editorListeners.remove(textEditor);
		if (listener != null) {
			IDocument doc = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput());
			doc.removeDocumentListener(listener);
			BcpLogger.info("Detached BcpDocumentListener from editor: " + textEditor.getTitle());
		}
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	public static BcPlugin getDefault() {
		return plugin;
	}

	private boolean isXmlOrXhtmlEditor(Object editor) {

		boolean result = false;

		if (editor instanceof ITextEditor textEditor
				&& textEditor.getEditorInput() instanceof IFileEditorInput fileEditorInput) {

			String fileName = fileEditorInput.getFile().getName().toLowerCase();

			if (fileName.endsWith(".xml") || fileName.endsWith(".xhtml")) {
				result = true;
			}
		}

		return result;
	}

}
