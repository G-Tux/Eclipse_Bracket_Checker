package eclipse.bcp.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import eclipse.bcp.BcPlugin;
import eclipse.bcp.constants.BcpConstants;

public class PrefPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public PrefPage() {
		super(GRID);
		setPreferenceStore(BcPlugin.getDefault().getPreferenceStore());
		setDescription("Configure how bracket problems are reported:");
	}

	@Override
	public void init(IWorkbench workbench) {
		// nothing to initialize
	}

	@Override
	protected void createFieldEditors() {
		String[][] values = new String[][] { { "Error", BcpConstants.ERROR }, { "Warning", BcpConstants.WARN },
				{ "Info", BcpConstants.INFO } };

		addField(new RadioGroupFieldEditor(BcpConstants.CONFIGURE_SEVERITY, "Severity Level", 1, values,
				getFieldEditorParent(), true));
	}
}
