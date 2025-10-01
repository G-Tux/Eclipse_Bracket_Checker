package eclipse.bcp.handlers;

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

import eclipse.bcp.constants.BcpConstants;
import eclipse.bcp.core.BcpCore;
import eclipse.bcp.data.BracketData;
import eclipse.bcp.logger.BcpLogger;
import eclipse.bcp.preferences.BcpPrefs;

public class BcpHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();

		if (editor instanceof ITextEditor textEditor) {

			IDocumentProvider provider = textEditor.getDocumentProvider();
			IDocument document = provider.getDocument(editor.getEditorInput());

			String content = document.get();

			List<BracketData> errors = BcpCore.findBracketErrors(content);

			clearProblemMarkers(editor);

			for (BracketData error : errors) {
				addProblemMarker(editor, error);
			}

		}

		return null;
	}

	private void clearProblemMarkers(IEditorPart editor) {
		if (editor.getEditorInput() instanceof FileEditorInput fileEditorInput) {
			IFile file = fileEditorInput.getFile();
			try {
				file.deleteMarkers(BcpConstants.BCP_PROBLEM_MARKER_ID, true, IFile.DEPTH_INFINITE);
			} catch (CoreException e) {
				BcpLogger.error("Failed to clear markers", e);
			}
		}
	}

	private void addProblemMarker(IEditorPart editor, BracketData bracketErrorData) {
		if (editor.getEditorInput() instanceof FileEditorInput fileEditorInput) {
			IFile file = fileEditorInput.getFile();
			try {
				IMarker marker = file.createMarker(BcpConstants.BCP_PROBLEM_MARKER_ID);
				marker.setAttribute(IMarker.SEVERITY, BcpPrefs.getSeverity());
				marker.setAttribute(IMarker.MESSAGE, bracketErrorData.getMessage());
				marker.setAttribute(IMarker.LINE_NUMBER, bracketErrorData.getLineNumber());
				marker.setAttribute(IMarker.CHAR_START, bracketErrorData.getCharStart());
				marker.setAttribute(IMarker.CHAR_END, bracketErrorData.getCharStart() + 1);

			} catch (CoreException e) {
				BcpLogger.error("Failed to add marker", e);
			}
		}
	}

}
