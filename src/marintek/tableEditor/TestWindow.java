package marintek.tableEditor;

import java.util.ArrayList;

import marintek.tableEditor.TestData.Entry;

import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class TestWindow extends Shell {

	private static final String COL_B = "ColB";
	private static final String COL_A = "ColA";
	private static String[] TableColumnNames = new String[] { COL_A, COL_B };

	

	private TestData testData;
//	private Table table;
	private TableViewer tableViewer;

	public TestWindow(Display display) {
		setSize(620, 479);
		// TODO Auto-generated constructor stub
		this.testData = new TestData();
		testData.createExample();
		setLayout(new GridLayout(1, false));

		Menu menu = new Menu(this, SWT.BAR);
		setMenuBar(menu);
		createContent(this);

		MenuItem mntmFile = new MenuItem(menu, SWT.CASCADE);
		mntmFile.setText("File");

		Menu menu_1 = new Menu(mntmFile);
		mntmFile.setMenu(menu_1);

		MenuItem mntmExit = new MenuItem(menu_1, SWT.NONE);
		mntmExit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				close();
			}
		});
		mntmExit.setText("Exit");

		MenuItem mntmRefresh = new MenuItem(menu, SWT.NONE);
		mntmRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tableViewer.refresh();
			}
		});
		mntmRefresh.setText("Refresh");
		
		

		//
		//
		tableViewer.setContentProvider(new ObservableListContentProvider());
		addLabelProvider(tableViewer);
		addCellModifier(tableViewer);
		
		//
		WritableList wl = new WritableList(testData.getDataTable(), TestData.Entry.class);
		tableViewer.setInput(wl);
		MenuItem mntmNewRow = new MenuItem(menu, SWT.NONE);
		mntmNewRow.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Entry rowAdded = testData.addRow();
				wl.add(rowAdded);
			}
		});
		mntmNewRow.setText("New Row");
		
		MenuItem mntmDebug = new MenuItem(menu, SWT.NONE);
		mntmDebug.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ArrayList<Entry> dt = testData.getDataTable();
				for(Entry et: dt)
				{
					System.out.println(et.toString());
				}
			}
		});
		mntmDebug.setText("Debug");
	}

	private void addCellModifier(TableViewer tabView) {
		tabView.setColumnProperties(TableColumnNames); // needed for modifier
		tabView.setCellModifier(new ICellModifier()
		{

			@Override
			public boolean canModify(Object element, String property) {
				// TODO Auto-generated method stub
				Entry e = (Entry) element;
				System.out.printf("canModify %s(%s)\n", e.toString(), property);
				return true;
			}

			@Override
			public Object getValue(Object element, String property) {
				Entry e = (Entry) element;
				System.out.printf("getValue %s(%s)\n", e.toString(), property);
				if (COL_A.equals(property))
					return e.getName();
				if (COL_B.equals(property))
					return String.format("%d", e.getNumber());
				return null;
			}

			@Override
			public void modify(Object element, String property, Object value) {
				// using the Entry from getValue call
				TableItem ti = (TableItem) element;
				 Entry e = (Entry) ti.getData();
				String str = (String) value;
				System.out.printf("modify %s(%s)=%s\n", e.toString(), property, str);
				if (COL_A.equals(property))
				{
					e.setName(str);
				}
				if (COL_B.equals(property))
				{
					e.setNumber(Integer.parseInt(str));
				}			
				tabView.refresh(e,true);
			}

		});
		// now adding editor part
		Table table = tableViewer.getTable();
		CellEditor[] editors = new CellEditor[2];
		editors[0] = new TextCellEditor(table);
		editors[1] = new TextCellEditor(table);
		tabView.setCellEditors(editors);
	}

	private void addLabelProvider(TableViewer tabView) {
		// table = tableViewer.getTable();
		tableViewer.setLabelProvider(new ITableLabelProvider() {
			@Override
			public Image getColumnImage(Object element, int columnIndex) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getColumnText(Object element, int columnIndex) {
				Entry e = (Entry) element;
				switch (columnIndex) {
				case 0:
					return e.getName();
				case 1:
					return String.format("%03d", e.getNumber());

				}
				return null;
			}

			@Override
			public void addListener(ILabelProviderListener listener) {
			}

			@Override
			public void dispose() {
			}

			@Override
			public boolean isLabelProperty(Object element, String property) {
				return false;
			}

			@Override
			public void removeListener(ILabelProviderListener listener) {
			}
		});

	}

	private void createContent(Composite parent) {
		// TODO Auto-generated method stub
		tableViewer = new TableViewer(parent, SWT.BORDER | SWT.FULL_SELECTION);
		Table table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnA = tableViewerColumn.getColumn();
		tblclmnA.setWidth(100);
		tblclmnA.setText(COL_A);
		

		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnB = tableViewerColumn_1.getColumn();
		tblclmnB.setWidth(100);
		tblclmnB.setText(COL_B);
		tblclmnB.setAlignment(SWT.CENTER);
		//
		//
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
