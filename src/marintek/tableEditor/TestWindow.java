package marintek.tableEditor;

import java.util.ArrayList;

import marintek.tableEditor.TestData.Entry;

import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class TestWindow extends Shell {

	private static final String COL_B = "ColB";
	private static final String COL_A = "ColA";

	

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

	private void createContent(Composite parent) {
		// TODO Auto-generated method stub
		tableViewer = new TableViewer(parent, SWT.BORDER | SWT.FULL_SELECTION);
		Table table = tableViewer.getTable();
		TextCellEditor te = new TextCellEditor(table);

		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		TableViewerColumn tvColA = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnA = tvColA.getColumn();
		tblclmnA.setWidth(100);
		tblclmnA.setText(COL_A);
		tvColA.setLabelProvider(new CellLabelProvider() {
			
			@Override
			public void update(ViewerCell cell) {
				Entry element = (Entry) cell.getElement();
				cell.setText(element.getName());
			}
		});
		tvColA.setEditingSupport(new EditingSupport(tableViewer) {
			
			@Override
			protected void setValue(Object element, Object value) {
				Entry e=(Entry) element;
				String str=(String) value;
				e.setName(str);
				tableViewer.refresh(e);
			}
			
			@Override
			protected Object getValue(Object element) {
				Entry e=(Entry) element;
				return e.getName();
			}
			
			@Override
			protected CellEditor getCellEditor(Object element) {
				// TODO Auto-generated method stub
				return te;
			}
			
			@Override
			protected boolean canEdit(Object element) {
				// TODO Auto-generated method stub
				return true;
			}
		});

		TableViewerColumn tvColB = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnB = tvColB.getColumn();
		tblclmnB.setWidth(100);
		tblclmnB.setText(COL_B);
		tblclmnB.setAlignment(SWT.CENTER);
		tvColB.setLabelProvider(new CellLabelProvider() {
			
			@Override
			public void update(ViewerCell cell) {
				Entry element = (Entry) cell.getElement();
				cell.setText(String.format("%04d", element.getNumber()));
				
			}
		});
		
		tvColB.setEditingSupport(new EditingSupport(tableViewer) {
			
			@Override
			protected void setValue(Object element, Object value) {
					Entry e=(Entry) element;
					String str=(String) value;
					e.setNumber(Integer.parseInt(str));
					tableViewer.refresh(e,true);
			}
			
			@Override
			protected Object getValue(Object element) {
				
				Entry e=(Entry) element;
				return String.format("%d", e.getNumber());
			}
			
			@Override
			protected CellEditor getCellEditor(Object element) {
				// TODO Auto-generated method stub
				return te;
			}
			
			@Override
			protected boolean canEdit(Object element) {
				// TODO Auto-generated method stub
				return true;
			}
		});
		//
		//
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
